package sql2omodel;

import dataobjects.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.simpleflatmapper.sql2o.SfmResultSetHandlerFactoryBuilder;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@FixMethodOrder(MethodSorters.JVM)
public class Sql2oModelTest {

    private static Sql2o sql2o;
    private static Model model;



    @BeforeClass
    public static void beforeClass(){
        sql2o = new Sql2o("jdbc:mysql://35.222.98.163:3306/DigiPet", "root", "digipet12345");
        model = new Sql2oModel(sql2o);
    }

    @Test
    public void createAdminTest(){
        model.createAdmin("User", "email@server.com", "password", true);
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM Administrator ORDER BY IdAdministrator DESC LIMIT 1");
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        Administrator admin =  query.executeAndFetchFirst(Administrator.class);
        Assert.assertEquals("User", admin.getUsername());
        Assert.assertEquals("email@server.com", admin.getEmail());
        Assert.assertEquals(true, admin.getStatus());

    }

    @Test
    public void getAllAdminsTest(){
        List<Administrator> admins = model.getAllAdmins();
        Connection connection = sql2o.open();
        Assert.assertEquals("User1", admins.get(0).getUsername());
        Assert.assertEquals("email@server.com", admins.get(0).getEmail());
        Assert.assertEquals(true, admins.get(0).getStatus());
    }

    @Test
    public void insertCaregiverTest(){
        String studentId = new Integer(new Random().nextInt()%100000000).toString();
        ArrayList<Integer> provinces = new ArrayList<>();
        provinces.add(2);
        provinces.add(3);

        try {
            model.insertCaregiver(studentId, 1, 1, 1,
                    "Juanito", "Cadillac", "email@server.com", "", "photo.jpg",
                    "lorem ipsum", 12345678, true, "password", provinces);
        } catch (Exception e) {
            Assert.assertNull(e);
        }

        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM Caregiver ORDER BY IdCaregiver DESC LIMIT 1");
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        Caregiver caregiver =  query.executeAndFetchFirst(Caregiver.class);
        Assert.assertEquals(studentId, caregiver.getIdStudent());
        Assert.assertEquals(1, caregiver.getIdUniversity());
        Assert.assertEquals("Juanito", caregiver.getName());

        try {
            model.insertCaregiver(studentId, 1, 1, 1,
                    "Juanito", "Cadillac", "email@server.com", "", "photo.jpg",
                    "lorem ipsum", 12345678, true, "password", provinces);
        } catch (Exception e) {
            Assert.assertEquals("Ya existe un usuario con este carné. Por favor intente con otro.", e.getMessage());
        }
    }

    @Test
    public void insertPetOwnerTest(){
        String email = new Integer(new Random().nextInt()).toString() +"@server.com";


        try {
            model.insertPetOwner(1, 1, "María", "Lamborghini",
                    email, "", 12345678, "photo.jpg", "lorem ipsum",
                    "password");
        } catch (Exception e) {
            Assert.assertNull(e);
        }

        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM PetOwner ORDER BY IdPetOwner DESC LIMIT 1");
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        PetOwner petOwner =  query.executeAndFetchFirst(PetOwner.class);
        Assert.assertEquals(email, petOwner.getEmail1());
        Assert.assertEquals(1, petOwner.getIdProvince());
        Assert.assertEquals("María", petOwner.getName());

        try {
            model.insertPetOwner(1, 1, "María", "Lamborghini",
                    email, "", 12345678, "photo.jpg", "lorem ipsum",
                    "password");
        } catch (Exception e) {
            Assert.assertEquals("Ya existe un usuario con este email. Por favor intente con otro.", e.getMessage());
        }
    }

    @Test
    public void insertPetTest(){
        ArrayList<String> photos = new ArrayList<>();
        photos.add("/photo1.jpg");
        photos.add("/photo2.jpg");
        String result1 = "";
        String result2 = "";
        String result3 = "";
        String result4 = "";
        int lastPetOwner = sql2o.beginTransaction()
                .createQuery("SELECT IdPetOwner FROM PetOwner ORDER BY IdPetOwner DESC LIMIT 1")
                .executeScalar(Integer.class);
        try {
            result1 = model.insertPet(lastPetOwner, "Clifford Bon Jovi Sr.", 10, "XL", "",
                    photos);
            result2 = model.insertPet(lastPetOwner, "Adele St. Gertrude Jr.", 5, "L", "",
                    photos);
            result3 = model.insertPet(lastPetOwner, "Joseph Little Finger II", 10, "M", "",
                    photos);
            result4 = model.insertPet(lastPetOwner, "Rubik von Dijkstra", 5, "S", "",
                    photos);
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }

        Assert.assertEquals("{\"status\":\"OK\"}", result1);
        Assert.assertEquals("{\"status\":\"OK\"}", result2);
    }

    @Test
    public void disablePetTest(){
        String result = "";
        try {
            result = model.disablePet(1);
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }

        Assert.assertEquals("{\"status\":\"OK\"}", result);
    }

    @Test
    public void insertHoursTest(){
        String result = "";
        int lastCaregiver = sql2o.beginTransaction()
                .createQuery("SELECT IdCaregiver FROM Caregiver ORDER BY IdCaregiver DESC LIMIT 1")
                .executeScalar(Integer.class);
        try {
            result = model.insertHours(lastCaregiver, "2019-06-17 12:00:00", "2019-06-17 18:00:00");
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }

        Assert.assertEquals("{\"status\":\"OK\"}", result);
    }

    @Test
    public void assignCaregiverAndAddServiceTest(){
        int lastPetOwner = sql2o.beginTransaction()
                .createQuery("SELECT IdPetOwner FROM PetOwner ORDER BY IdPetOwner DESC LIMIT 1")
                .executeScalar(Integer.class);
        int lastPet = sql2o.beginTransaction()
                .createQuery("SELECT IdPet FROM Pet ORDER BY IdPet DESC LIMIT 1")
                .executeScalar(Integer.class);
        int caregiver1 = 0;
        String result1 = "";
        int caregiver2 = 0;
        String result2 = "";
        int caregiver3 = 0;
        String result3 = "";
        int caregiver4 = 0;
        String result4 = "";

        caregiver1 = model.assignCaregiver(lastPet, lastPetOwner, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "");
        result1 = model.insertService(lastPet, caregiver1, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "Cuidado con el perro","En la casa" );
        caregiver2 = model.assignCaregiver(lastPet-1, lastPetOwner, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "");
        result2 = model.insertService(lastPet-1, caregiver2, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "Cuidado con los gatos","En el parque" );
        caregiver3 = model.assignCaregiver(lastPet-2, lastPetOwner, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "");
        result3 = model.insertService(lastPet-2, caregiver3, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "Cuidado con los caballos","En la casa" );
        caregiver4 = model.assignCaregiver(lastPet-3, lastPetOwner, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "");
        result4 = model.insertService(lastPet-3, caregiver4, "2019-06-17 12:00:00", "2019-06-17 18:00:00", "Cuidado con los mordiscos","En la casa" );

        System.out.println(caregiver1);
        System.out.println(caregiver2);
        System.out.println(caregiver3);
        System.out.println(caregiver4);

    }

    @Test
    public void getClientIdTypeTest(){
        String result1 = "";
        String result2 = "";
        String result3 = "";
        try {
            result1 = model.getClientIdType("User1", "password");
            result2 = model.getClientIdType("1236431635@server.com", "password");
            result3 = model.getClientIdType("436476072", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result1.contains("Admin"));
        Assert.assertTrue(result2.contains("Client"));
        Assert.assertTrue(result3.contains("Student"));

    }

    @Test
    public void updateRatingTest() throws Exception{
        ObjectNode result = null;
        result = model.updateRate(1, 4);
        Assert.assertNotNull(result);
    }

    @Test
    public void getAllPetsFromOwnerTest(){
        List<Pet> result = null;

        result = model.getAllPetsFromOwner(6);

        Assert.assertEquals(4, result.size());
    }

    @Test
    public void getServicesFromOwnerTest() throws Exception{
        List<WalkService> result = model.getServicesForPetOwner(19);
        Assert.assertEquals(4, result.size());

    }

    @Test
    public void getOwnerFromIdTest() throws Exception{
        PetOwner result = model.getPetOwnerFromId(1);
        Assert.assertEquals(1, result.getIdPetOwner());
    }

    @Test
    public void getPetFromIdTest() throws Exception{
        Pet result = model.getPetFromId(1);
        Assert.assertEquals(1, result.getIdPet());
    }

    @Test
    public void getServicesForPetTest() throws Exception{
        List result = model.getServicesForPet(53);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void getCaregiverFromIdTest() throws Exception{
        Caregiver result = model.getCaregiverFromId(1);
        Assert.assertEquals(1, result.getIdCaregiver());
    }

    @Test
    public void getServicesForCaregiverTest() throws Exception{
        List result = model.getServicesForCaregiver(5);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void getComplaintsTest() throws Exception{
        List result = model.getComplaints();
        Assert.assertTrue(result.size()>0);
    }

    @Test
    public void getReportTest() throws Exception{
        List result = model.getReport("2019-06-17 12:00:00", "2019-06-17 13:00:00");
        Assert.assertTrue( result.size()>=0);
    }

    @Test
    public void blockCaregiverTest() throws Exception{
        String result = model.blockCaregiver(3);
        Assert.assertEquals("{\"status\":\"OK\"}", result);
    }

    @Test
    public void getServiceFromIdTest() throws Exception{
        WalkService result = model.getServiceFromId(1);
        Assert.assertEquals(1, result.getIdWalkService());
    }

    @Test
    public void getServicePriceTest() throws Exception{
        String result = model.getServicePrice("2019-06-13 12:00:00", "2019-06-13 13:00:00");
        Assert.assertEquals("{\"price\":60}", result);
    }

    @Test
    public void insertComplaintTest() throws Exception{
        int lastComplaint = sql2o.beginTransaction()
                .createQuery("SELECT IdWalkService FROM Complaint ORDER BY IdWalkService DESC LIMIT 1")
                .executeScalar(Integer.class);
        String result = model.insertComplaint(lastComplaint+1, "Muy malo");
        Assert.assertEquals("{\"status\":\"OK\"}", result);
    }

    @Test
    public void updateReportTest() throws Exception{
        String result = model.updateReport(1, "Caminamos bastante");
        Assert.assertEquals("{\"status\":\"OK\"}", result);
    }

    @Test
    public void getScheduleTest() throws Exception{
        List<Schedule> result = model.getAllScheduleEntries(1, "2019-06-13 05:00:00");
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void changePetOwnerStatusTest() throws Exception{
        String result = model.changePetOwnerStatus(1, true);
        Assert.assertEquals("{\"status\":\"OK\"}", result);
    }
}

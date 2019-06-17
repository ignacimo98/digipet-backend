package sql2omodel;

import dataobjects.Administrator;
import dataobjects.Caregiver;
import dataobjects.Model;
import dataobjects.PetOwner;
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
        Assert.assertEquals("User", admins.get(0).getUsername());
        Assert.assertEquals("email@server.com", admins.get(0).getEmail());
        Assert.assertEquals(true, admins.get(0).getStatus());
    }

    @Test
    public void insertCaregiverTest(){
        String studentId = new Integer(new Random().nextInt()).toString();
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
        String result = "";
        try {
            result = model.insertPet(1, "Clifford Bon Jovi Sr.", 10, "XL", "",
                    photos);
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }

        Assert.assertEquals("{\"status\":\"OK\"}", result);
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



}

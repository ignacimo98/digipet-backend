package dataobjects;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WalkServiceDetailedTest {

    public static WalkServiceDetailed walkServiceDetailed;

    @BeforeClass
    public static void beforeClass() throws Exception {
        walkServiceDetailed = new WalkServiceDetailed();
    }


    @Test
    public void testPOJO(){
        walkServiceDetailed.setIdCaregiver(1);
        Assert.assertEquals(1, walkServiceDetailed.getIdCaregiver());
        walkServiceDetailed.setStudentName("Juan");
        Assert.assertEquals("Juan", walkServiceDetailed.getStudentName());
        walkServiceDetailed.setStudentLastName("Cadillac");
        Assert.assertEquals("Cadillac", walkServiceDetailed.getStudentLastName());
        walkServiceDetailed.setEmailStudent("student@server.com");
        Assert.assertEquals("student@server.com", walkServiceDetailed.getEmailStudent());
        walkServiceDetailed.setStudentPhoto("/photo1.jpg");
        Assert.assertEquals("/photo1.jpg", walkServiceDetailed.getStudentPhoto());
        walkServiceDetailed.setWalksQuantity(1);
        Assert.assertEquals(1, walkServiceDetailed.getWalksQuantity());
        walkServiceDetailed.setWalksRating(4.5f);
        Assert.assertTrue(walkServiceDetailed.getWalksRating()==4.5f);
        walkServiceDetailed.setPhoneStudent(12345678);
        Assert.assertEquals(12345678, walkServiceDetailed.getPhoneStudent());
        walkServiceDetailed.setBadges(new ArrayList());
        Assert.assertEquals(0, walkServiceDetailed.getBadges().size());

        walkServiceDetailed.setClientName("Carlitos");
        Assert.assertEquals("Carlitos", walkServiceDetailed.getClientName());
        walkServiceDetailed.setClientLastName("Gutierritos");
        Assert.assertEquals("Gutierritos", walkServiceDetailed.getClientLastName());
        walkServiceDetailed.setEmailClient("client@server.com");
        Assert.assertEquals("client@server.com", walkServiceDetailed.getEmailClient());
        walkServiceDetailed.setClientPhone(87654321);
        Assert.assertEquals(87654321, walkServiceDetailed.getClientPhone());
        walkServiceDetailed.setClientPhoto("/photo2.jpg");
        Assert.assertEquals("/photo2.jpg", walkServiceDetailed.getClientPhoto());

        walkServiceDetailed.setIdPet(1);
        Assert.assertEquals(1, walkServiceDetailed.getIdPet());
        walkServiceDetailed.setPetName("Firulais");
        Assert.assertEquals("Firulais", walkServiceDetailed.getPetName());
        walkServiceDetailed.setAge(1);
        Assert.assertEquals(1, walkServiceDetailed.getAge());
        walkServiceDetailed.setSize("M");
        Assert.assertEquals("M", walkServiceDetailed.getSize());
        walkServiceDetailed.setPetDescription("Muerde");
        Assert.assertEquals("Muerde", walkServiceDetailed.getPetDescription());
        walkServiceDetailed.setPhotoLinks(new ArrayList());
        Assert.assertEquals(0, walkServiceDetailed.getPhotoLinks().size());

        walkServiceDetailed.setStartTime("2019-06-13 06:00:00");
        Assert.assertEquals("2019-06-13 06:00:00", walkServiceDetailed.getStartTime());
        walkServiceDetailed.setEndTime("2019-06-13 07:00:00");
        Assert.assertEquals("2019-06-13 07:00:00", walkServiceDetailed.getEndTime());
        walkServiceDetailed.setPrice(60);
        Assert.assertEquals(60, walkServiceDetailed.getPrice());
        walkServiceDetailed.setOwnerComments("Muerde");
        Assert.assertEquals("Muerde", walkServiceDetailed.getOwnerComments());
        walkServiceDetailed.setPickUpLocation("Aquí");
        Assert.assertEquals("Aquí", walkServiceDetailed.getPickUpLocation());
        walkServiceDetailed.setReportDescription("OK");
        Assert.assertEquals("OK", walkServiceDetailed.getReportDescription());
        walkServiceDetailed.setWalkServiceStatus(true);
        Assert.assertEquals(true, walkServiceDetailed.isWalkServiceStatus());
        walkServiceDetailed.setRating(4);
        Assert.assertEquals(4, walkServiceDetailed.getRating());

    }


}
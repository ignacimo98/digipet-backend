package dataobjects;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

public class AdministratorTest {
    public static Administrator administrator;

    @BeforeClass
    public static void beforeClass(){
        administrator = new Administrator();
    }

    @Test
    public void testIdAdministrator(){
        int id  = new Random().nextInt();
        administrator.setIdAdministrator(id);
        Assert.assertEquals(id, administrator.getIdAdministrator());
    }

    @Test
    public void testUsername(){
        String username = "username";
        administrator.setUsername(username);
        Assert.assertEquals(username, administrator.getUsername());
    }

    @Test
    public void testEmail(){
        String email = "email@server.com";
        administrator.setEmail(email);
        Assert.assertEquals(email, administrator.getEmail());
    }

    @Test
    public void testPassword(){
        String password = "password";
        administrator.setPassword(password);
        Assert.assertEquals(password, administrator.getPassword());
    }

    @Test
    public void testStatus(){
        boolean status = true;
        administrator.setStatus(status);
        Assert.assertEquals(status, administrator.getStatus());
    }

}

package dataobjects;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class BadgeTest {
    public static Badge badge;

    @BeforeClass
    public static void beforeClass(){
        badge = new Badge();
    }

    @Test
    public void testPOJO(){
        badge.setBadgeType(1);
        Assert.assertEquals(1, badge.getBadgeType());
        badge.setIdCaregiver(1);
        Assert.assertEquals(1, badge.getIdCaregiver());
    }
}
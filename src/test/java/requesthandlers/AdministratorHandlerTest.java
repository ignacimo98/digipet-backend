package requesthandlers;

import dataobjects.Model;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Sql2o;
import routing.CustomResponse;
import routing.ResponseCreator;
import sql2omodel.Sql2oModel;

public class AdministratorHandlerTest {
    private static Sql2o sql2o = new Sql2o("jdbc:mysql://35.222.98.163:3306/DigiPet", "root", "digipet12345");
    private static Model model;

    @BeforeClass
    public static void beforeClass(){
        model = new Sql2oModel(sql2o);
    }

    @Test
    public void testGetAdmin(){
        ResponseCreator response = AdministratorHandler.getAdmin(model, 1);
        System.out.println(response);
    }
}

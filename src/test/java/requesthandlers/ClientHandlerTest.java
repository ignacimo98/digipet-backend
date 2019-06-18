package requesthandlers;

import dataobjects.Model;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Sql2o;
import routing.ResponseCreator;
import sql2omodel.Sql2oModel;

import static org.junit.Assert.*;

public class ClientHandlerTest {

    private static Sql2o sql2o = new Sql2o("jdbc:mysql://35.222.98.163:3306/DigiPet", "root", "digipet12345");
    private static Model model;

    @BeforeClass
    public static void beforeClass(){
        model = new Sql2oModel(sql2o);
    }

    @Test
    public void getPetOwner() {
        ResponseCreator response = ClientHandler.getPetOwner(model, 1);
    }

    @Test
    public void getPets() {
        ResponseCreator response = ClientHandler.getPets(model, 1);
    }
}
package dataobjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import routing.ResponseCreator;
import routing.CustomResponse;

import java.io.IOException;

@Data
public class Administrator {
    private int IdAdministrator;
    private String Username;
    private String Email;
    private String Password;
    private boolean Status;

    public Administrator() {};
    public Administrator(int IdAdministrator, String Username, String Email, String Password, boolean Status){
        this.IdAdministrator = IdAdministrator;
        this.Username = Username;
        this.Email = Email;
        this.Password = Password;
        this.Status = Status;
    }

    public int getIdAdministrator() {
        return IdAdministrator;
    }

    public void setIdAdministrator(int idAdministrator) {
        IdAdministrator = idAdministrator;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public static ResponseCreator getAllAdmins(Model model) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(model.getAllAdmins());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonString = "{}";
        }

        System.out.println(jsonString);
        return CustomResponse.ok(jsonString);
    }

    public static ResponseCreator insertAdmin(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        Administrator creation = null;
        try {
            creation = mapper.readValue(requestBody, Administrator.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //int id = model.createAdmin("user", "djaidjias", "sdada", 0);
        int id = model.createAdmin(creation.getUsername(), creation.getEmail(), creation.getPassword(), creation.getStatus());
        return CustomResponse.ok(id);
    }
}

package dataobjects;

import lombok.Data;

@Data
public class Administrator {
    private int IdAdministrator;
    private String Username;
    private String Email;
    private String Password;
    private int Status;

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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}

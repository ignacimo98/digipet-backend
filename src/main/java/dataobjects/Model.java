package dataobjects;

import java.util.List;

public interface Model {
    int createAdmin(String username, String email, String password, int status);

    List getAllAdmins();
}
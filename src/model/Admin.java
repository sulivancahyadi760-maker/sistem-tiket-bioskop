package model;

public class Admin extends User {

    public Admin(String username, String password, int saldo) {
        super(username, password, saldo);
    }

    @Override
    public String getRole() {
        return "admin";
    }

}

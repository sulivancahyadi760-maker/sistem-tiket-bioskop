package model;

public class Customer extends User {

    public Customer(String username, String password, int saldo) {
        super(username, password, saldo);
    }

    @Override
    public String getRole() {
        return "customer";
    }

}

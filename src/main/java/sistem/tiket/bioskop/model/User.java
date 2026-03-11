package sistem.tiket.bioskop.model;

public abstract class User {
    private String username;
    private String password;
    private int saldo;

    public User(String username, String password, int saldo) {
        this.username = username;
        this.password = password;
        this.saldo = saldo;
    }

    public abstract String getRole();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void addSaldo(int jml) {
        if (jml > 0) {
            this.saldo += jml;
        }
    }
}
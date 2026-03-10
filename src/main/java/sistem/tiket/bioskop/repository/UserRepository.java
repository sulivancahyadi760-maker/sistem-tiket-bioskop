package sistem.tiket.bioskop.repository;

import sistem.tiket.bioskop.model.Admin;
import sistem.tiket.bioskop.model.Customer;
import sistem.tiket.bioskop.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();

    public UserRepository() {
        users.add(new Admin("Raasyid", "Rasyid123", 9000));
        users.add(new Customer("Rillah", "Rillah456", 500000));
        users.add(new Customer("Ega", "Rillah456", 700000));
        users.add(new Customer("KIKI", "Rillah456", 800000));
        users.add(new Customer("Sulivan", "Sulivan124", 500000));
        users.add(new Customer("Masrian", "Masrian121", 500000));
        users.add(new Admin("admin", "admin", 100000000));
        users.add(new Customer("cust", "cust", 10000000));

    }

    public User findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null; // user tidak ditemukan
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return users;
    }
}

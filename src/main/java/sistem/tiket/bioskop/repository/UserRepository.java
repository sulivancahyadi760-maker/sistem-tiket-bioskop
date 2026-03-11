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
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean deleteUser(String username) {
        return users.removeIf(u -> u.getUsername().equals(username));
    }

    public List<User> getAllUsers() {
        return users;
    }
}

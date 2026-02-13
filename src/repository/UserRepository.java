package repository;

import model.Admin;
import model.Customer;
import model.User;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> users = new ArrayList<>();

    public UserRepository() {

        users.add(new Admin("Raasyid", "Rasyid123", 9000));
        users.add(new Customer("Rillah", "Rillah456", 1000));
        users.add(new Customer("Ega", "Rillah456", 7000));
        users.add(new Customer("KIKI", "Rillah456", 100123));
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

    public ArrayList<User> getAllUsers() {
        return users;
    }
}

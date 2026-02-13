package repository;

import model.User;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> users = new ArrayList<>();

    public UserRepository() {

        users.add(new User("Raasyid", "Rasyid123", 9000, "admin"));
        users.add(new User("Rillah", "Rillah456", 1000, "customer"));
        users.add(new User("Ega", "Rillah456", 7000, "customer"));
        users.add(new User("KIKI", "Rillah456", 100123, "customer"));
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

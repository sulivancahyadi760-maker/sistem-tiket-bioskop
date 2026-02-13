package repository;

import model.User;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> users = new ArrayList<>();

    public UserRepository() {

        users.add(new User("admin", "admin123", 0, "admin"));
        users.add(new User("user1", "user123", 100000, "customer"));
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

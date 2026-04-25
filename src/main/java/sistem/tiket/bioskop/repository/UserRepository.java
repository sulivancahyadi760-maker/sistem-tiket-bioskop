package sistem.tiket.bioskop.repository;

import sistem.tiket.bioskop.model.Admin;
import sistem.tiket.bioskop.model.Customer;
import sistem.tiket.bioskop.model.User;
import sistem.tiket.bioskop.utils.CSVUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();
    private final String CSV_PATH = "src/main/java/sistem/tiket/bioskop/data/users.csv";

    public UserRepository() {
        loadDataCSV();
    }

    public void loadDataCSV() {
        List<User> loaded = CSVUtils.read(CSV_PATH, data -> {
            if (data.length != 4)
                return null;
            String user = data[0].trim(), pass = data[1].trim(), role = data[3].trim();
            int saldo = Integer.parseInt(data[2].trim());

            return role.equalsIgnoreCase("admin") ? new Admin(user, pass, saldo) : new Customer(user, pass, saldo);
        });

        if (loaded.isEmpty()) {
            users.add(new Admin("admin", "admin", 10000000));
            saveDataToCSV();
        } else {
            this.users = new ArrayList<>(loaded);
        }
    }

    public void saveDataToCSV() {
        CSVUtils.write(CSV_PATH, users,
                u -> u.getUsername() + "," + u.getPassword() + "," + u.getSaldo() + "," + u.getRole());
    }

    public User findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
        saveDataToCSV();
    }

    public boolean deleteUser(String username) {
        boolean removed = users.removeIf(u -> u.getUsername().equals(username));
        if (removed)
            saveDataToCSV();
        return removed;
    }

    public List<User> getAllUsers() {
        return users;
    }
}

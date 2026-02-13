package service;

import model.User;
import repository.UserRepository;

public class AuthService {
    private UserRepository userRepo;

    public AuthService(UserRepository repo) {
        this.userRepo = repo;
    }

    // Login method
    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            System.out.println("User tidak ditemukan!");
            return null;
        }

        if (user.getPassword().equals(password)) {
            System.out.println("Login berhasil sebagai " + user.getRole());
            return user;
        } else {
            System.out.println("Password salah!");
            return null;
        }
    }

    // Logout
    public void logout(User user) {
        System.out.println("User " + user.getUsername() + " logout.");
    }

    // Regis user baru
    public boolean register(String username, String password, int saldo, String role) {
        if (userRepo.findByUsername(username) != null) {
            System.out.println("Username sudah ada!");
            return false;
        }
        User newUser = new User(username, password, saldo, role);
        userRepo.addUser(newUser);
        System.out.println("User berhasil didaftarkan!");
        return true;
    }
}

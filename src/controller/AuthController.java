package controller;

import model.Admin;
import model.Customer;
import model.User;
import repository.UserRepository;

public class AuthController {
    private UserRepository userRepo;
    private User currentUser;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            System.out.println("User tidak ditemukan!");
            return null;
        }

        if (user.getPassword().equals(password)) {
            System.out.println("Login berhasil sebagai " + user.getRole());
            this.currentUser = user;
            return user;
        } else {
            System.out.println("Password salah!");
            return null;
        }
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("User " + currentUser.getUsername() + " logout.");
            this.currentUser = null;
        } else {
            System.out.println("Tidak ada user yang sedang login.");
        }
    }

    public boolean register(String username, String password, int saldo, String role) {
        if (userRepo.findByUsername(username) != null) {
            System.out.println("Username sudah ada!");
            return false;
        }

        User newUser;
        if ("admin".equalsIgnoreCase(role)) {
            newUser = new Admin(username, password, saldo);
        } else {
            newUser = new Customer(username, password, saldo);
        }

        userRepo.addUser(newUser);
        System.out.println("User berhasil didaftarkan sebagai " + role);
        return true;
    }

    public User getCurrentUser() {
        return currentUser;
    }

}

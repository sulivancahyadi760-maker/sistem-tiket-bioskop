package controller;

import model.Admin;
import model.Customer;
import model.User;
import service.AuthService;

public class AuthController {
    private AuthService authService;
    private User currentUser;

    public AuthController(AuthService service) {
        this.authService = service;
    }

    public void login(String username, String password) {
        User user = authService.login(username, password);
        if (user != null) {
            currentUser = user;
            if (user instanceof Admin) {
                System.out.println("Masuk ke halaman dashboard");
            } else if (user instanceof Customer) {
                System.out.println("Masuk ke halaman pemesanan");
            }
        } else {
            System.out.println("Login gagal");
        }
    }

    public void logout() {
        if (currentUser != null) {
            authService.logout(currentUser);
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

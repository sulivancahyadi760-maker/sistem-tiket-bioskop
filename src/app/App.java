package app;

import repository.*;
import controller.*;

/*
 * ini adalah app atau main untuk menjalankan project
 */

public class App {
    public static void main(String[] args) {

        UserRepository userRepo = new repository.UserRepository();
        MovieRepository movieRepo = new repository.MovieRepository();
        ScheduleRepository schRepo = new repository.ScheduleRepository();
        TiketRepository tiketRepo = new repository.TiketRepository();

        AuthController authCtrl = new controller.AuthController(userRepo);
        MovieController movieCtrl = new controller.MovieController(movieRepo);
        ScheduleController schCtrl = new controller.ScheduleController(schRepo);
        BookingController bookCtrl = new controller.BookingController(tiketRepo);

        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Gagal pasang FlatLaf");
        }

        java.awt.EventQueue.invokeLater(() -> {
            view.MainFrame frame = new view.MainFrame(authCtrl, movieCtrl, schCtrl, bookCtrl);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
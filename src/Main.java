import model.*;
import controller.*;
import repository.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        var userRepo = new UserRepository();
        var tiketRepo = new TiketRepository();
        var movieRepo = new MovieRepository();
        var scheduleRepo = new ScheduleRepository();

        var bookingService = new BookingService(tiketRepo);
        var scheduleService = new ScheduleService(scheduleRepo);
        var bookingController = new BookingController(bookingService);

        // simulasi pemesanan tiket
        Movie film = movieRepo.findMovie("Interstellar");
        Studio studio = scheduleRepo.getAllStudios().get(0);
        scheduleService.addSchedule(film, studio, "14:00");

        Customer customer = (Customer) userRepo.findByUsername("KIKI");
        Schedule jadwal = scheduleService.getAllSchedules().get(0);

        boolean berhasil = bookingController.pesanTiket(customer, jadwal, "A-12", 45000);

        // kondisi kalo berhasil cetak tiket
        if (berhasil) {
            System.out.println("Status: Berhasil dipesan ");
            var tiketKiki = tiketRepo.findByUsername("KIKI");
            tiketKiki.get(0).printTiket();
        } else {
            System.out.println("Status: Gagal dipesan");
        }
    }
}
package sistem.tiket.bioskop.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sistem.tiket.bioskop.model.*;
import sistem.tiket.bioskop.repository.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CinemaRestController {

    private final AuthController authController;
    private final MovieController movieController;
    private final ScheduleController scheduleController;
    private final BookingController bookingController;
    private final MovieRepository movieRepo;
    private final ScheduleRepository schRepo;
    private final UserRepository userRepo;
    private final TiketRepository tiketRepo;

    public CinemaRestController() {
        this.userRepo = new UserRepository();
        this.movieRepo = new MovieRepository();
        this.schRepo = new ScheduleRepository();
        this.tiketRepo = new TiketRepository();

        this.authController = new AuthController(userRepo);
        this.movieController = new MovieController(movieRepo);
        this.scheduleController = new ScheduleController(schRepo);
        this.bookingController = new BookingController(tiketRepo);
    }

    public static class LoginReq {
        public String username;
        public String password;
    }

    public static class RegReq {
        public String username;
        public String password;
        public int saldo;
        public String role;
    }

    public static class MovieReq {
        public String namaFilm;
        public int durasi;
        public String genre;
    }

    public static class ScheduleReq {
        public String namaFilm;
        public String namaStudio;
        public String jamTayang;
    }

    public static class BookingReq {
        public String username;
        public String namaFilm;
        public String namaStudio;
        public String jamTayang;
        public String seat;
    }

    public static class UserResponseDTO {
        public String username;
        public String role;
        public int saldo;

        public UserResponseDTO(User user) {
            this.username = user.getUsername();
            this.role = user.getRole();
            if (user instanceof Customer) {
                this.saldo = ((Customer) user).getSaldo();
            } else {
                this.saldo = 0;
            }
        }
    }

    // Helper untuk standarisasi response
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (data != null)
            response.put("data", data);
        return ResponseEntity.status(status).body(response);
    }

    // ================= AUTHENTICATION =================
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        User user = authController.login(req.username, req.password);
        if (user != null) {
            return buildResponse(HttpStatus.OK, "Login berhasil", new UserResponseDTO(user));
        }
        return buildResponse(HttpStatus.UNAUTHORIZED, "Username atau password salah", null);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegReq req) {
        boolean success = authController.register(req.username, req.password, req.saldo, req.role);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Registrasi berhasil", null);
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Username sudah digunakan", null);
    }

    // ================= MOVIES =================
    @GetMapping("/movies")
    public ResponseEntity<?> getMovies() {
        return buildResponse(HttpStatus.OK, "Berhasil mengambil data film", movieController.getAllMovies());
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestBody MovieReq req) {
        boolean success = movieController.addFilm(req.namaFilm, req.durasi, req.genre);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Film berhasil ditambahkan", null);
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Gagal menambahkan film. Data tidak valid atau sudah ada.", null);
    }

    @DeleteMapping("/movies/{namaFilm}")
    public ResponseEntity<?> deleteMovie(@PathVariable String namaFilm) {
        boolean success = movieController.deleteFilm(namaFilm);
        if (success) {
            return buildResponse(HttpStatus.OK, "Film berhasil dihapus", null);
        }
        return buildResponse(HttpStatus.NOT_FOUND, "Film tidak ditemukan", null);
    }

    // ================= STUDIOS & SCHEDULES =================
    @GetMapping("/studios")
    public ResponseEntity<?> getStudios() {
        return buildResponse(HttpStatus.OK, "Berhasil mengambil data studio", scheduleController.getAllStudios());
    }

    @GetMapping("/schedules")
    public ResponseEntity<?> getSchedules() {
        return buildResponse(HttpStatus.OK, "Berhasil mengambil jadwal tayang", scheduleController.getAllSchedules());
    }

    @PostMapping("/schedules")
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleReq req) {
        Movie mv = movieRepo.findMovie(req.namaFilm);
        Studio std = schRepo.getAllStudios().stream()
                .filter(s -> s.getNamaStudio().equalsIgnoreCase(req.namaStudio))
                .findFirst().orElse(null);

        if (mv == null || std == null) {
            return buildResponse(HttpStatus.BAD_REQUEST, "Movie atau Studio tidak valid", null);
        }

        boolean success = scheduleController.addSchedule(mv, std, req.jamTayang);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Jadwal berhasil ditambahkan", null);
        }
        return buildResponse(HttpStatus.CONFLICT, "Jadwal bentrok / gagal ditambahkan", null);
    }

    // ================= BOOKING =================
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingReq req) {
        User user = userRepo.findByUsername(req.username);

        if (!(user instanceof Customer)) {
            return buildResponse(HttpStatus.FORBIDDEN, "Hanya customer yang bisa melakukan booking", null);
        }

        Schedule sch = schRepo.getAllSchedule().stream()
                .filter(s -> s.getMovie().getNamaFilm().equalsIgnoreCase(req.namaFilm) &&
                        s.getStudio().getNamaStudio().equalsIgnoreCase(req.namaStudio) &&
                        s.getJamTayang().equalsIgnoreCase(req.jamTayang))
                .findFirst()
                .orElse(null);

        if (sch == null) {
            return buildResponse(HttpStatus.NOT_FOUND, "Jadwal tidak ditemukan", null);
        }

        boolean success = bookingController.pesanTiket((Customer) user, sch, req.seat);

        if (success) {
            return buildResponse(HttpStatus.CREATED, "Tiket berhasil dipesan", null);
        }

        // Catatan: Ini masih general karena controller asli mereturn boolean.
        return buildResponse(HttpStatus.BAD_REQUEST, "Booking gagal. Saldo kurang atau kursi sudah terisi.", null);
    }

    @GetMapping("/users/{username}/bookings")
    public ResponseEntity<?> getUserBookings(@PathVariable String username) {
        List<Tiket> tiketList = bookingController.getTiketByUsername(username);
        return buildResponse(HttpStatus.OK, "Berhasil mengambil riwayat tiket", tiketList);
    }

    // ================= USER ===============
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return buildResponse(HttpStatus.OK, "Berhasil mengambil data user", userRepo.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody RegReq req) {
        boolean success = authController.register(req.username, req.password, req.saldo, req.role);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Registrasi berhasil", null);
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Username sudah digunakan", null);
    }
}
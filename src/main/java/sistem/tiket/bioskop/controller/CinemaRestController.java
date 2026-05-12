package sistem.tiket.bioskop.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sistem.tiket.bioskop.model.*;
import sistem.tiket.bioskop.repository.*;
import sistem.tiket.bioskop.service.*;
import sistem.tiket.bioskop.dto.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CinemaRestController {

    private final AuthService authService;
    private final MovieService movieService;
    private final ScheduleService scheduleService;
    private final BookingService bookingService;
    
    private final MovieRepository movieRepo;
    private final ScheduleRepository schRepo;
    private final UserRepository userRepo;
    private final TiketRepository tiketRepo;

    public CinemaRestController(AuthService authService, MovieService movieService, 
                               ScheduleService scheduleService, BookingService bookingService,
                               MovieRepository movieRepo, ScheduleRepository schRepo,
                               UserRepository userRepo, TiketRepository tiketRepo) {
        this.authService = authService;
        this.movieService = movieService;
        this.scheduleService = scheduleService;
        this.bookingService = bookingService;
        this.movieRepo = movieRepo;
        this.schRepo = schRepo;
        this.userRepo = userRepo;
        this.tiketRepo = tiketRepo;
    }

    /**
     * membuat sebuah response entity dengan status, message, dan data.
     * jika data tidak null, maka data akan dimasukkan ke dalam response.
     * 
     * @param status  status dari response
     * @param message pesan dari response
     * @param data    data yang akan dikirimkan
     * @return response entity yang dibuat
     */
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
        User user = authService.login(req.username, req.password);
        if (user != null) {
            return buildResponse(HttpStatus.OK, "Login berhasil", new UserResponseDTO(user));
        }
        return buildResponse(HttpStatus.UNAUTHORIZED, "Username atau password salah", null);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegReq req) {
        boolean success = authService.register(req.username, req.password, req.saldo, req.role);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Registrasi berhasil", null);
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Username sudah digunakan", null);
    }

    // ================= MOVIES =================
    @GetMapping("/movies")
    public ResponseEntity<?> getMovies() {
        return buildResponse(HttpStatus.OK, "Berhasil mengambil data film", movieService.getAllMovies());
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestBody MovieReq req) {
        boolean success = movieService.addFilm(req.namaFilm, req.durasi, req.genre, req.posterUrl);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Film berhasil ditambahkan", null);
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Gagal menambahkan film. Data tidak valid atau sudah ada.", null);
    }

    @DeleteMapping("/movies/{namaFilm}")
    public ResponseEntity<?> deleteMovie(@PathVariable String namaFilm) {
        boolean success = movieService.deleteFilm(namaFilm);
        if (success) {
            return buildResponse(HttpStatus.OK, "Film berhasil dihapus", null);
        }
        return buildResponse(HttpStatus.NOT_FOUND, "Film tidak ditemukan", null);
    }

    // ================= STUDIOS & SCHEDULES =================
    @GetMapping("/studios")
    public ResponseEntity<?> getStudios() {
        return buildResponse(HttpStatus.OK, "Berhasil mengambil data studio", scheduleService.getAllStudios());
    }

    @GetMapping("/schedules")
    public ResponseEntity<?> getSchedules() {
        List<Schedule> tickets = scheduleService.getAllSchedules();
        List<ScheduleDTO> dtoList = tickets.stream().map(ScheduleDTO::new).collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "Berhasil mengambil jadwal tayang", dtoList);
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

        boolean success = scheduleService.addSchedule(mv, std, req.jamTayang);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Jadwal berhasil ditambahkan", null);
        }
        return buildResponse(HttpStatus.CONFLICT, "Jadwal bentrok / gagal ditambahkan", null);
    }

    @DeleteMapping("/schedules")
    public ResponseEntity<?> deleteSchedule(@RequestBody ScheduleReq req) {
        boolean success = scheduleService.deleteSchedule(req.namaFilm, req.namaStudio, req.jamTayang);
        if (success) {
            return buildResponse(HttpStatus.OK, "Jadwal berhasil dihapus", null);
        }
        return buildResponse(HttpStatus.NOT_FOUND, "Jadwal tidak ditemukan", null);
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

        boolean success = bookingService.pesanTiket((Customer) user, sch, req.seat);

        if (success) {
            userRepo.saveDataToCSV();
            return buildResponse(HttpStatus.CREATED, "Tiket berhasil dipesan", null);
        }

        // Catatan: Ini masih general karena controller asli mereturn boolean.
        return buildResponse(HttpStatus.BAD_REQUEST, "Booking gagal. Saldo kurang atau kursi sudah terisi.", null);
    }

    @GetMapping("/users/{username}/bookings")
    public ResponseEntity<?> getUserBookings(@PathVariable String username) {
        List<Tiket> tiketList = bookingService.getTiketByUsername(username);
        List<TiketDTO> dtoList = tiketList.stream().map(TiketDTO::new).collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "Berhasil mengambil riwayat tiket", dtoList);
    }

    @GetMapping("/tickets")
    public ResponseEntity<?> getAllTickets() {
        List<Tiket> tiketList = tiketRepo.getAllTiket();
        List<TiketDTO> dtoList = tiketList.stream().map(TiketDTO::new).collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "Berhasil mengambil semua riwayat tiket", dtoList);
    }

    // ================= USER ===============
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return buildResponse(HttpStatus.OK, "Berhasil mengambil data user", userRepo.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody RegReq req) {
        boolean success = authService.register(req.username, req.password, req.saldo, req.role);
        if (success) {
            return buildResponse(HttpStatus.CREATED, "Registrasi berhasil", null);
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Username sudah digunakan", null);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        boolean success = userRepo.deleteUser(username);
        if (success) {
            return buildResponse(HttpStatus.OK, "User berhasil dihapus", null);
        }
        return buildResponse(HttpStatus.NOT_FOUND, "User tidak ditemukan", null);
    }

    @PostMapping("/users/topup")
    public ResponseEntity<?> topUpSaldo(@RequestBody TopUpReq req) {
        User user = userRepo.findByUsername(req.username);

        if (user == null) {
            return buildResponse(HttpStatus.NOT_FOUND, "User tidak ditemukan", null);
        }

        if (req.jumlah <= 0) {
            return buildResponse(HttpStatus.BAD_REQUEST, "Saldo harus lebih besar dari 0", null);
        }

        user.addSaldo(req.jumlah);
        userRepo.saveDataToCSV();
        return buildResponse(HttpStatus.OK, "Top up berhasil. Saldo sekarang: " + user.getSaldo(), null);
    }
}
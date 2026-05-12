package sistem.tiket.bioskop.service;

import java.util.List;

import sistem.tiket.bioskop.model.Customer;
import sistem.tiket.bioskop.model.Schedule;
import sistem.tiket.bioskop.model.Tiket;
import sistem.tiket.bioskop.repository.TiketRepository;

import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private TiketRepository tiketRepo;

    public BookingService(TiketRepository tiketRepo) {
        this.tiketRepo = tiketRepo;
    }

    public boolean isSeatAvailable(Schedule schedule, String seat) {
        for (Tiket t : tiketRepo.getAllTiket()) {
            if (t.getJadwalFilm().equals(schedule) &&
                    t.getSeat().equalsIgnoreCase(seat)) {
                return false;
            }
        }
        return true;
    }

    public boolean pesanTiket(Customer cust, Schedule sch, String seat) {
        if (!isSeatAvailable(sch, seat)) {
            System.out.println("Kursi sudah terisi!");
            return false;
        }
        int harga = sch.getHarga();

        if (cust.getSaldo() < harga) {
            System.out.println("Saldo tidak mencukupi! Harga: Rp" + harga + ", Saldo anda: Rp" + cust.getSaldo());
            return false;
        }

        cust.setSaldo(cust.getSaldo() - harga);
        Tiket newTiket = new Tiket(cust, sch, seat, harga);

        tiketRepo.addTiket(newTiket);
        return true;
    }

    public List<Tiket> getTiketByUsername(String username) {
        return tiketRepo.findByUsername(username);
    }
}
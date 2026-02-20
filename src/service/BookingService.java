package service;

import model.Customer;
import model.Schedule;
import model.Tiket;
import repository.TiketRepository;

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

    public boolean pesanTiket(Customer cust, Schedule sch, String seat, int harga) {
        if (!isSeatAvailable(sch, seat)) {
            return false;
        }
        if (cust.getSaldo() < harga) {
            return false;
        }
        cust.setSaldo(cust.getSaldo() - harga);

        int newId = tiketRepo.getAllTiket().size() + 1;
        Tiket newTiket = new Tiket(newId, cust, sch, seat, harga);

        tiketRepo.addTiket(newTiket);

        return true;
    }

}

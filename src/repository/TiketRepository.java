package repository;

import java.util.ArrayList;
import java.util.List;

import model.Schedule;
import model.Tiket;
import model.User;

public class TiketRepository {
    private List<Tiket> tikets = new ArrayList<>();

    public void addTiket() {
        UserRepository custRepo = new UserRepository();
        ScheduleRepository schRepo = new ScheduleRepository();

        List<User> custs = custRepo.getAllUsers();
        List<Schedule> schedules = schRepo.getAllSchedule();

        tikets.add(new Tiket(1, custs.get(0), schedules.get(0), "A-10", 45000));
        tikets.add(new Tiket(2, custs.get(1), schedules.get(1), "B-12", 45000));
        tikets.add(new Tiket(3, custs.get(2), schedules.get(2), "D-7", 45000));
        tikets.add(new Tiket(4, custs.get(4), schedules.get(3), "F-18", 60000));
        tikets.add(new Tiket(5, custs.get(5), schedules.get(4), "G-9", 60000));
    }

    public Tiket findTiket(String datatiket) {
        for (Tiket t : tikets) {
            if (t.getJadwalFilm().getMovie().getNamaFilm().equalsIgnoreCase(datatiket)) {
                return t;
            }
        }
        return null;
    }

    public void addTiket(Tiket tiket) {
        tikets.add(tiket);
    }

    public List<Tiket> getAllTiket() {
        return tikets;
    }

    public List<Tiket> findByUsername(String username) {
        List<Tiket> hasil = new ArrayList<>();
        for (Tiket t : tikets) {
            if (t.getUser() != null &&
                    t.getUser().getUsername().equals(username)) {

                hasil.add(t);
            }
        }
        return hasil;
    }
}

package sistem.tiket.bioskop.repository;

import java.util.ArrayList;
import java.util.List;

import sistem.tiket.bioskop.model.Schedule;
import sistem.tiket.bioskop.model.Tiket;
import sistem.tiket.bioskop.model.User;
import sistem.tiket.bioskop.utils.CSVUtils;

public class TiketRepository {
    private List<Tiket> tikets = new ArrayList<>();
    private final String CSV_PATH = "src/main/java/sistem/tiket/bioskop/data/tikets.csv";

    public void loadDataCSV(UserRepository userRepo, ScheduleRepository schRepo) {
        this.tikets = CSVUtils.read(CSV_PATH, data -> {
            if (data.length != 6)
                return null;
            User user = userRepo.findByUsername(data[0].trim());
            Schedule sch = schRepo.findSchedule(data[1].trim(), data[2].trim(), data[3].trim());
            String seat = data[4].trim();
            int harga = Integer.parseInt(data[5].trim());

            if (user != null && sch != null) {
                return new Tiket(user, sch, seat, harga);
            }

            return null;
        });
    }

    public void saveDataToCSV() {
        CSVUtils.write(CSV_PATH, tikets, t -> t.getUser().getUsername() + "," +
                t.getJadwalFilm().getMovie().getNamaFilm() + "," +
                t.getJadwalFilm().getStudio().getNamaStudio() + "," +
                t.getJadwalFilm().getJamTayang() + "," +
                t.getSeat() + "," +
                t.getHarga());
    }

    public void addTiket(Tiket tiket) {
        tikets.add(tiket);
        saveDataToCSV();
    }

    public Tiket findTiket(String datatiket) {
        for (Tiket t : tikets) {
            if (t.getJadwalFilm().getMovie().getNamaFilm().equalsIgnoreCase(datatiket)) {
                return t;
            }
        }
        return null;
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

package sistem.tiket.bioskop.repository;

import java.util.ArrayList;
import java.util.List;

import sistem.tiket.bioskop.model.Movie;
import sistem.tiket.bioskop.model.Schedule;
import sistem.tiket.bioskop.model.Studio;
import sistem.tiket.bioskop.utils.CSVUtils;

public class ScheduleRepository {
    private List<Schedule> schedules = new ArrayList<>();
    private List<Studio> studios = new ArrayList<>();
    private final String PATH_STUDIO = "src/main/java/sistem/tiket/bioskop/data/studios.csv";
    private final String PATH_SCHEDULES = "src/main/java/sistem/tiket/bioskop/data/schedules.csv";

    public ScheduleRepository(MovieRepository movieRepo) {
        loadStudiosCSV();
        loadSchedulesCSV(movieRepo);

    }

    public void loadStudiosCSV() {
        this.studios = CSVUtils.read(PATH_STUDIO, data -> {
            if (data.length != 3)
                return null;
            return new Studio(data[0].trim(), Integer.parseInt(data[1].trim()), data[2].trim());
        });
    }

    public void loadSchedulesCSV(MovieRepository mvRepo) {
        this.schedules = CSVUtils.read(PATH_SCHEDULES, data -> {
            if (data.length != 3)
                return null;
            Movie mv = mvRepo.findMovie(data[0].trim());
            Studio std = findStudioByName(data[1].trim());
            return (mv != null && std != null) ? new Schedule(mv, std, data[2].trim()) : null;
        });
    }

    public void saveDataToCSV() {
        CSVUtils.write(PATH_SCHEDULES, schedules, s -> s.getMovie().getNamaFilm() + "," +
                s.getStudio().getNamaStudio() + "," +
                s.getJamTayang());
    }

    public Studio findStudioByName(String name) {
        return studios.stream()
                .filter(s -> s.getNamaStudio().equals(name))
                .findFirst().orElse(null);
    }

    public Schedule findSchedule(String movie, String studioName, String time) {
        return schedules.stream()
                .filter(s -> s.getMovie().getNamaFilm().equalsIgnoreCase(movie)
                        && s.getStudio().getNamaStudio().equals(studioName)
                        && s.getJamTayang().equals(time))
                .findFirst().orElse(null);

    }

    public void addSchedule(Schedule sch) {
        schedules.add(sch);
        saveDataToCSV();
    }

    public void deleteSchedule(Schedule sch) {
        schedules.remove(sch);
        saveDataToCSV();
    }

    public List<Schedule> getAllSchedule() {
        return schedules;
    }

    public List<Studio> getAllStudios() {
        return studios;
    }

}

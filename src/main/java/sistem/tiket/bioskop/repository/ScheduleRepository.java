package sistem.tiket.bioskop.repository;

import java.util.ArrayList;
import java.util.List;

import sistem.tiket.bioskop.model.Movie;
import sistem.tiket.bioskop.model.Schedule;
import sistem.tiket.bioskop.model.Studio;

public class ScheduleRepository {
    private List<Schedule> schedules = new ArrayList<>();
    private List<Studio> studios = new ArrayList<>();

    public ScheduleRepository() {

        studios.add(new Studio("Studio 1", 50, "Reguler"));
        studios.add(new Studio("Studio 2", 50, "Reguler"));
        studios.add(new Studio("Studio IMAX", 80, "Premium"));
        studios.add(new Studio("Studio VIP", 20, "VIP"));

        schedules.add(new Schedule(new Movie("Inception", 148, "Sci-Fi"), studios.get(0),
                "10:00"));
        schedules.add(new Schedule(new Movie("Inception", 148, "Sci-Fi"), studios.get(1),
                "13:00"));
        schedules.add(new Schedule(new Movie("The Dark Knight", 152, "Action"),
                studios.get(2), "16:00"));
        schedules.add(new Schedule(new Movie("Interstellar", 169, "Adventure"),
                studios.get(3), "19:00"));
        schedules.add(new Schedule(new Movie("Spirited Away", 125, "Animation"),
                studios.get(0), "20:00"));
    }

    public void addSchedule(Schedule sch) {
        schedules.add(sch);
    }

    public List<Schedule> getAllSchedule() {
        return schedules;
    }

    public List<Studio> getAllStudios() {
        return studios;
    }

}

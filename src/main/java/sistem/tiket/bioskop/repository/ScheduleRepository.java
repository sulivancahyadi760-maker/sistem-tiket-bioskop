package sistem.tiket.bioskop.repository;

import java.util.ArrayList;
import java.util.List;

import sistem.tiket.bioskop.model.Schedule;
import sistem.tiket.bioskop.model.Studio;

public class ScheduleRepository {
    private List<Schedule> schedules = new ArrayList<>();
    private List<Studio> studios = new ArrayList<>();

    public ScheduleRepository(MovieRepository movieRepo) {

        studios.add(new Studio("Studio 1", 50, "Reguler"));
        studios.add(new Studio("Studio 2", 50, "Reguler"));
        studios.add(new Studio("Studio IMAX", 80, "Premium"));
        studios.add(new Studio("Studio VIP", 20, "VIP"));

        schedules.add(new Schedule(movieRepo.findMovie("Inception"), studios.get(0), "10:00"));
        schedules.add(new Schedule(movieRepo.findMovie("Inception"), studios.get(1), "13:00"));
        schedules.add(new Schedule(movieRepo.findMovie("The Dark Knight"), studios.get(2), "16:00"));
        schedules.add(new Schedule(movieRepo.findMovie("Interstellar"), studios.get(3), "19:00"));
        schedules.add(new Schedule(movieRepo.findMovie("Spirited Away"), studios.get(0), "20:00"));
    }

    public void addSchedule(Schedule sch) {
        schedules.add(sch);
    }

    public void deleteSchedule(Schedule sch) {
        schedules.remove(sch);
    }

    public List<Schedule> getAllSchedule() {
        return schedules;
    }

    public List<Studio> getAllStudios() {
        return studios;
    }

}

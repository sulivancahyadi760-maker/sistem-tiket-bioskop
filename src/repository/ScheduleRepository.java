package repository;

import java.util.ArrayList;
import java.util.List;

import model.Schedule;
import model.Studio;

public class ScheduleRepository {
    private List<Schedule> schedules = new ArrayList<>();
    private List<Studio> studios = new ArrayList<>();

    public ScheduleRepository() {
        studios.add(new Studio( "Studio 1", 50, "Reguler"));
        studios.add(new Studio( "Studio 2", 50, "Reguler"));
        studios.add(new Studio( "Studio IMAX", 80, "Premium"));
        studios.add(new Studio( "Studio VIP", 20, "VIP"));
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

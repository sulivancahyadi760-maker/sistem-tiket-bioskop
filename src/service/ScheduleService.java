package service;

import java.util.List;

import model.Movie;
import model.Schedule;
import model.Studio;
import repository.ScheduleRepository;

public class ScheduleService {
    private ScheduleRepository schRepo;

    public ScheduleService(ScheduleRepository schRepo) {
        this.schRepo = schRepo;
    }

    public boolean addSchedule(Movie mv, Studio std, String jamTayang) {
        if (mv == null || std == null || jamTayang == null || jamTayang.trim().isEmpty()) {
            return false;
        }

        for (Schedule s : schRepo.getAllSchedule()) {
            if (s.getStudio().getStudioId() == std.getStudioId() && s.getJamTayang().equalsIgnoreCase(jamTayang)) {
                System.out.println("Studio sudah dipakai di jam tayang tersebut");
                return false;
            }
        }

        int newId = schRepo.getAllSchedule().size() + 1;
        Schedule newSchedule = new Schedule(newId, mv, std, jamTayang);

        schRepo.addSchedule(newSchedule);
        return true;
    }

    public List<Schedule> getAllSchedules() {
        return schRepo.getAllSchedule();
    }

    public List<Studio> getAllStudios() {
        return schRepo.getAllStudios();
    }

}

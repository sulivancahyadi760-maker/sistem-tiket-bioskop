package controller;

import java.util.List;

import model.Movie;
import model.Schedule;
import model.Studio;
import service.ScheduleService;

public class ScheduleController {
    private ScheduleService schService;

    public ScheduleController(ScheduleService schService) {
        this.schService = schService;
    }

    public boolean addSchedule(Movie mv, Studio std, String jamTayang) {
        return schService.addSchedule(mv, std, jamTayang);
    }

    public List<Studio> getAllStudios() {
        return schService.getAllStudios();
    }

    public List<Schedule> getAllSchedules() {
        return schService.getAllSchedules();
    }

}
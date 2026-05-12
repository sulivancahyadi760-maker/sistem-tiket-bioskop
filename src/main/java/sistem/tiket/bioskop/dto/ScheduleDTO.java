package sistem.tiket.bioskop.dto;

import sistem.tiket.bioskop.model.Movie;
import sistem.tiket.bioskop.model.Schedule;
import sistem.tiket.bioskop.model.Studio;

public class ScheduleDTO {
    public Movie movie;
    public Studio studio;
    public String jamTayang;
    public int harga;

    public ScheduleDTO(Schedule s) {
        this.movie = s.getMovie();
        this.studio = s.getStudio();
        this.jamTayang = s.getJamTayang();
        this.harga = s.getHarga();
    }
}

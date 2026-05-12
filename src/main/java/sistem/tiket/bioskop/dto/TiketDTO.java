package sistem.tiket.bioskop.dto;

import sistem.tiket.bioskop.model.Tiket;

public class TiketDTO {
    public String username;
    public String namaFilm;
    public String namaStudio;
    public String jamTayang;
    public String seat;
    public int harga;

    public TiketDTO(Tiket t) {
        this.username = t.getUser() != null ? t.getUser().getUsername() : "-";
        this.namaFilm = t.getJadwalFilm() != null && t.getJadwalFilm().getMovie() != null
                ? t.getJadwalFilm().getMovie().getNamaFilm()
                : "-";
        this.namaStudio = t.getJadwalFilm() != null && t.getJadwalFilm().getStudio() != null
                ? t.getJadwalFilm().getStudio().getNamaStudio()
                : "-";
        this.jamTayang = t.getJadwalFilm() != null ? t.getJadwalFilm().getJamTayang() : "-";
        this.seat = t.getSeat();
        this.harga = t.getHarga();
    }
}

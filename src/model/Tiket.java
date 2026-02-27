package model;

public class Tiket {
    private User user;
    private Schedule jadwalFilm;
    private String seat;
    private int harga;

    // Konstruktor
    public Tiket(User user, Schedule jadwalFilm, String seat, int harga) {
        this.user = user;
        this.jadwalFilm = jadwalFilm;
        this.seat = seat;
        this.harga = harga;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Schedule getJadwalFilm() {
        return jadwalFilm;
    }

    public void setJadwalFilm(Schedule jadwalFilm) {
        this.jadwalFilm = jadwalFilm;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void printTiket() {
        System.out.println("===== RINGKASAN PEMESANAN TIKET =====");
        System.out.println("Nama Pembeli : " + getUser().getUsername());
        System.out.println("Film         : " + getJadwalFilm().getMovie().getNamaFilm());
        System.out.println("Studio       : " + getJadwalFilm().getStudio().getNamaStudio());
        System.out.println("Jam Tayang   : " + getJadwalFilm().getJamTayang());
        System.out.println("Nomor Kursi  : " + getSeat());
        System.out.println("Total Harga  : Rp" + getHarga());
        System.out.println("=====================================");
    }

}

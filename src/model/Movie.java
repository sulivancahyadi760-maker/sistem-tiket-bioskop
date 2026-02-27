package model;

public class Movie {
    private String namaFilm;
    private int durasi;
    private String genre;

    public Movie( String namaFilm, int durasi, String genre) {
        this.namaFilm = namaFilm;
        this.durasi = durasi;
        this.genre = genre;
    }

    public String getNamaFilm() {
        return namaFilm;
    }

    public void setNamaFilm(String namaFilm) {
        this.namaFilm = namaFilm;
    }

    public int getDurasi() {
        return durasi;
    }

    public void setDurasi(int durasi) {
        this.durasi = durasi;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}

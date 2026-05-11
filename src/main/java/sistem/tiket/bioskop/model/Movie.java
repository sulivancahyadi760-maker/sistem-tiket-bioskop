package sistem.tiket.bioskop.model;

public class Movie {
    private String namaFilm;
    private int durasi;
    private String genre;
    private String posterUrl;

    public Movie(String namaFilm, int durasi, String genre, String posterUrl) {
        this.namaFilm = namaFilm;
        this.durasi = durasi;
        this.genre = genre;
        this.posterUrl = posterUrl;
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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}

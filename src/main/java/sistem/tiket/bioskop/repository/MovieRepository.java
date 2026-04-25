package sistem.tiket.bioskop.repository;

import java.util.ArrayList;
import java.util.List;

import sistem.tiket.bioskop.model.Movie;
import sistem.tiket.bioskop.utils.CSVUtils;

public class MovieRepository {
    List<Movie> movieList = new ArrayList<>();
    private final String CSV_PATH = "src/main/java/sistem/tiket/bioskop/data/movies.csv";

    public MovieRepository() {
        loadDataCSV();
    }

    public void loadDataCSV() {
        this.movieList = CSVUtils.read(CSV_PATH, data -> {
            if (data.length != 3)
                return null;
            return new Movie(data[0].trim(), Integer.parseInt(data[1].trim()), data[2].trim());
        });
    }

    public void saveDataToCSV() {
        CSVUtils.write(CSV_PATH, movieList, mv -> mv.getNamaFilm() + "," + mv.getDurasi() + "," + mv.getGenre());
    }

    public Movie findMovie(String namaFilm) {
        return movieList.stream().filter(m -> m.getNamaFilm()
                .equalsIgnoreCase(namaFilm))
                .findFirst().orElse(null);
    }

    public List<Movie> getAllMovies() {
        return movieList;
    }

    public void addFilm(Movie mv) {
        movieList.add(mv);
        saveDataToCSV();
    }

    public void deleteFilm(Movie mv) {
        movieList.remove(mv);
        saveDataToCSV();
    }
}

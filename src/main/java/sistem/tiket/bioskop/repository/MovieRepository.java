package sistem.tiket.bioskop.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import sistem.tiket.bioskop.model.Movie;
import sistem.tiket.bioskop.utils.CSVUtils;

@Repository
public class MovieRepository {
    List<Movie> movieList = new ArrayList<>();
    private final String CSV_PATH = "src/main/java/sistem/tiket/bioskop/data/movies.csv";

    public MovieRepository() {
        loadDataCSV();
    }

    public void loadDataCSV() {
        this.movieList = CSVUtils.read(CSV_PATH, data -> {
            if (data.length < 3)
                return null;
            String poster = (data.length >= 4) ? data[3].trim() : "";
            return new Movie(data[0].trim(), Integer.parseInt(data[1].trim()), data[2].trim(), poster);
        });
    }

    public void saveDataToCSV() {
        CSVUtils.write(CSV_PATH, movieList, mv -> mv.getNamaFilm() + "," + mv.getDurasi() + "," + mv.getGenre() + "," + mv.getPosterUrl());
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

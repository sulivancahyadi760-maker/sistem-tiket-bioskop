package controller;

import java.util.List;
import java.util.stream.Collectors;
import model.Movie;
import repository.MovieRepository;

public class MovieController {
    private MovieRepository movieRepo;

    public MovieController(MovieRepository movieRepo) {
        this.movieRepo = movieRepo;
    }

    public boolean addFilm(String namaFilm, int durasi, String genre) {
        if (durasi <= 0 || namaFilm.trim().isEmpty()) {
            System.out.println("Data film tidak valid");
            return false;
        }

        for (Movie m : movieRepo.getAllMovies()) {
            if (m.getNamaFilm().equalsIgnoreCase(namaFilm)) {
                return false;
            }
        }

        Movie newMovie = new Movie(namaFilm, durasi, genre);
        movieRepo.addFilm(newMovie);
        return true;
    }

    public boolean deleteFilm(String namaFilm) {
        Movie movie = movieRepo.findMovie(namaFilm);
        if (movie != null) {
            movieRepo.deleteFilm(movie);
            return true;
        }
        return false;
    }

    public List<Movie> filterByGenre(String genre) {
        return movieRepo.getAllMovies().stream()
                .filter(mv -> mv.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Movie> getAllMovies() {
        return movieRepo.getAllMovies();
    }

}
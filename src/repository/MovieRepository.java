package repository;

import java.util.ArrayList;
import java.util.List;

import model.Movie;

public class MovieRepository {
    List<Movie> movieList = new ArrayList<>();

    public MovieRepository() {
        movieList.add(new Movie(1, "Inception", 148, "Sci-Fi"));
        movieList.add(new Movie(2, "The Dark Knight", 152, "Action"));
        movieList.add(new Movie(3, "Interstellar", 169, "Adventure"));
        movieList.add(new Movie(4, "Spirited Away", 125, "Animation"));
        movieList.add(new Movie(5, "The Godfather", 175, "Crime"));
    }

    public Movie findMovie(String namaFilm) {
        for (Movie m : movieList) {
            if (m.getNamaFilm().equals(namaFilm)) {
                return m;
            }
        }
        return null;
    }

    public List<Movie> getAllMovies() {
        return movieList;
    }

    public void addFilm(Movie mv) {
        movieList.add(mv);
    }

    public void deleteFilm(Movie mv) {
        movieList.remove(mv);
    }
}

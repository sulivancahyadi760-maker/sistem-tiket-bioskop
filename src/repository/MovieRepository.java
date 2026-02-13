package repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Movie;

public class MovieRepository {
    List<Movie> movieList = new ArrayList<>();

    public MovieRepository() {
        Movie movie1 = new Movie(1, "Stand by Me Doraemon", 120, "Animasi");
        Movie movie2 = new Movie(2, "Avatar : Fire and Ash", 200, "Science Fiction");
        Movie movie3 = new Movie(3, "Stand by Me Doraemon", 120, "Animasi");
        Movie movie4 = new Movie(4, "Stand by Me Doraemon", 120, "Animasi");
        Movie movie5 = new Movie(5, "Stand by Me Doraemon", 120, "Animasi");
        movieList.addAll(Arrays.asList(movie1, movie2, movie3, movie4, movie4, movie5));

    }

    public Movie finMovie(String namaFilm) {
        for (Movie m : movieList) {
            if (m.getNamaFilm().equals(namaFilm)) {
                return m;
            }
        }
        return null;
    }

    public List<Movie> getAllMovies(){
        return movieList;
    }
}

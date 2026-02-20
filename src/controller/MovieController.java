package controller;

import service.MovieService;

public class MovieController {
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public boolean addFilm(String namaFilm, int durasi, String genre) {
        return movieService.addFilm(namaFilm, durasi, genre);
    }

    public boolean deleteFilm(String namaFilm) {
        return movieService.deleteFilm(namaFilm);
    }

}

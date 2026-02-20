package controller;

import java.util.List;

import model.Movie;
import repository.MovieRepository;

public class MovieController {
    //tambah hapus film

    private MovieRepository movieRepo;
    private List<Movie> seluruhMovie; 
    
    MovieController(MovieRepository movieRepo){
        this.movieRepo = movieRepo;
        this.seluruhMovie = this.movieRepo.getAllMovies();
    }

    public void addFilm(Movie movie){
        if(!seluruhMovie.contains(movie)){
            seluruhMovie.add(movie);
        }
    }

    public void deleteFilm(String namaFilm){
        for(Movie mv : seluruhMovie){
            if(mv.getNamaFilm().equals(namaFilm)){
                seluruhMovie.remove(mv);
                break;
            }
        }
    }
}

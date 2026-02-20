package service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import model.Movie;
import repository.MovieRepository;

public class MovieService {
    //logic filter film berdasarkan genrenya

    private MovieRepository movieRepo;
    private List<Movie> seluruhMovie; 

    public MovieService(MovieRepository movieRepo){
        this.movieRepo = movieRepo;
        this.seluruhMovie = this.movieRepo.getAllMovies();        
    }

    public List<Movie> filterByGenre(String genre){
        List<Movie> movieByGenre = seluruhMovie.stream().filter(movie -> movie.getGenre().equals(genre)).collect(Collectors.toList());
        return movieByGenre;
    }
    
}

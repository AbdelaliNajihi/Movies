package www.movies.com.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import www.movies.com.model.Movie;

public interface IMovieService {
	 List<Movie> getMovies(String key);
	 Movie getMovie(String title);
	 Movie saveMovie(Movie m, MultipartFile file) throws IllegalStateException, IOException;
	 long countMovies();
	 Movie findByTitle(String title);
	 boolean checkIfMovieExists(String title);
}

package www.movies.com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import www.movies.com.model.Movie;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, String> {
	List<Movie> findByTitleContainingOrderByReleaseDateAsc(String key);
	Movie findByTitle(String title);
}

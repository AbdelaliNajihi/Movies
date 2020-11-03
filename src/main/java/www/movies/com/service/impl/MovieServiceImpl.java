package www.movies.com.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import www.movies.com.dao.IMovieRepository;
import www.movies.com.model.Movie;
import www.movies.com.service.IMovieService;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieServiceImpl implements IMovieService {
	private final IMovieRepository movieRepository;
	@Value("${posters}")
	private String posters;

	@Override
	public List<Movie> getMovies(String key) {
		return movieRepository.findByTitleContainingOrderByReleaseDateAsc(key);
	}

	@Override
	public Movie getMovie(String title) {
		return movieRepository.getOne(title);
	}

	@Override
	public Movie saveMovie(Movie m, MultipartFile file) throws IllegalStateException, IOException {
		File postersDirectory = new File(posters);
		if (!postersDirectory.exists()) {
			postersDirectory.mkdirs();
		}
		if (file != null && !file.isEmpty()) {
			m.setPoster(file.getOriginalFilename());
			movieRepository.save(m);
			file.transferTo(new File(posters + m.getTitle()));
			// Files.write(Paths.get(posters + m.getId()), file.getBytes());
		}
		return movieRepository.save(m);
	}

	@Override
	public long countMovies() {
		return movieRepository.count();
	}

	@Override
	public Movie findByTitle(String title) {
		return movieRepository.findByTitle(title);
	}

	@Override
	public boolean checkIfMovieExists(String title) {
		if (findByTitle(title) != null) {
			return true;
		}
		return false;
	}
}

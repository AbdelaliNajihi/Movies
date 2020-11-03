package www.movies.com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import www.movies.com.model.Movie;
import www.movies.com.service.IMovieService;

@Controller
@RequestMapping(value = "/movie")
@RequiredArgsConstructor
public class MovieController {
	private final Logger logger = LoggerFactory.getLogger(MovieController.class);
	private final IMovieService movieService;
	@Value("${posters}")
	private String posters;

	@GetMapping(value = "/new")
	public String newMovie(Model model) {
		model.addAttribute("movie", new Movie());
		List<String> genres = Arrays.asList("romance", "crime", "comedy");
		model.addAttribute("genres", genres);
		return "movie";
	}

	@GetMapping(value = "/details")
	public String movieDetails(String title, Model model) {
		File poster = new File(posters + title);
		Movie movie = movieService.getMovie(title);
		model.addAttribute("movie", movie);
		if (poster.exists() || poster.isFile()) {
			model.addAttribute("hasPoster", true);
		}
		System.out.println(poster.exists()); System.out.println(poster.isFile());
		return "movieDetails";
	}

	@GetMapping(value = "/poster")
	@ResponseBody
	public byte[] getPoster(@RequestParam(name = "title") String title, Model model)
			throws FileNotFoundException, IOException {
		logger.info("poster displayed !");
		File poster = new File(posters + title);
		return IOUtils.toByteArray(new FileInputStream(poster));
	}

}

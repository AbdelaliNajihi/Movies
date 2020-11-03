package www.movies.com.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import www.movies.com.model.Movie;
import www.movies.com.service.IMovieService;

@Controller
@RequiredArgsConstructor
public class NewMovieController {
	private final IMovieService movieService;

	@PostMapping(value = "/addmovie")
	public String addMovie(@Valid Movie m, BindingResult bindingResult, @RequestParam(name = "file") MultipartFile file,
			Model model, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {
		List<String> genres = Arrays.asList("romance", "crime", "comedy");
		model.addAttribute("genres", genres);
		if (bindingResult.hasErrors()) {
			return "movie";
			/*
			 * } else if (file.isEmpty()) { model.addAttribute("fileEmpty", true); return
			 * "movie";
			 */
		} else if (movieService.checkIfMovieExists(m.getTitle())) {
			model.addAttribute("movieExists", true);
			return "movie";
		} else
			redirectAttributes.addFlashAttribute("success", "Movie successfully saved");
		movieService.saveMovie(m, file);
		return "redirect:/movie/new";
	}
}

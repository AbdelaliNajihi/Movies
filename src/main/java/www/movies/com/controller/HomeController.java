package www.movies.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import www.movies.com.service.IMovieService;
import www.movies.com.service.IUserAppService;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final IMovieService movieService;
	private final IUserAppService userAppService;

	@GetMapping(value = "/home")
	public String home(Model model, @RequestParam(name = "key", defaultValue = "") String key) {
		model.addAttribute("movies", movieService.getMovies(key));
		model.addAttribute("key", key);
		model.addAttribute("moviesNumber", movieService.countMovies());
		return "home";
	}

}

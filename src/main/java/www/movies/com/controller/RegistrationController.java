package www.movies.com.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import www.movies.com.model.UserApp;
import www.movies.com.service.IUserAppService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
	private final IUserAppService userAppService;
	List<String> genders = Arrays.asList("male", "female");

	@GetMapping(value = "/registration")
	public String registrationPage(Model model) {
		model.addAttribute("userApp", new UserApp());
		model.addAttribute("gender", genders);
		return "registration";
	}

	@PostMapping(value = "/register")
	public String registration(@Valid UserApp u, BindingResult bindingResult,
			@RequestParam(name = "file") MultipartFile file, Model model, RedirectAttributes redirectAttributes)
			throws IllegalStateException, IOException {
		model.addAttribute("gender", genders);
		if (bindingResult.hasErrors()) {
			return "registration";
		} else if (userAppService.checkIfUsernameExists(u.getUsername())) {
			model.addAttribute("duplicateUsername", true);
			return "registration";
		} else if (userAppService.checkIfEmailExists(u.getEmail())) {
			model.addAttribute("duplicateEmail", true);
			return "registration";
		} /*
			 * else if (file.isEmpty()) { model.addAttribute("noFile", true); return
			 * "registration"; }
			 */else
			redirectAttributes.addFlashAttribute("succRegistration", true);
			userAppService.saveUser(u, file);
		return "redirect:/login";
	}
}

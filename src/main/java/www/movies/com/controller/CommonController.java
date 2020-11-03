package www.movies.com.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import www.movies.com.model.UserApp;
import www.movies.com.service.IUserAppService;

@Controller
@RequiredArgsConstructor
public class CommonController {
	private final IUserAppService userAppService;
	@Value("${userImages}")
	private String userImages;
	
	@GetMapping(value = "/navbar")
	public String navbar(String username, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		username = authentication.getName();
		UserApp userApp = userAppService.findUserByUsername(username);
		model.addAttribute("userApp", userApp);
		File userImagesDirectory = new File(userImages + username);
		if (userImagesDirectory.exists() || userImagesDirectory.isFile()) {
			model.addAttribute("userHasImage", true);
		}
		System.out.println(userImagesDirectory.exists()); System.out.println(userImagesDirectory.isFile());
		return "fragments/navbar";
	}
	
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/login?logout";
	}
	
	@GetMapping(value = "/users")
	public String allUsers(Model model) {
		model.addAttribute("users", userAppService.getAllUsers());
		return "users";
	}
}

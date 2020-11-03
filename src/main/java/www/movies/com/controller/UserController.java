package www.movies.com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import www.movies.com.model.UserApp;
import www.movies.com.service.IUserAppService;
import www.movies.com.utility.ChangePassword;

@Controller
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
	private final IUserAppService userAppService;
	@Value("${userImages}")
	private String userImages;
	List<String> genders = new ArrayList<>(Arrays.asList("male", "female"));
	private final PasswordEncoder passwordEncoder;

	@GetMapping(value = "/profile")
	public String profile(Model model, Principal principal) {
		UserApp userApp = userAppService.findUserByUsername(principal.getName());
		File userImage = new File(userImages);
		model.addAttribute("userApp", userApp);
		if (userImage.exists() || userImage.isFile()) {
			model.addAttribute("hasAnImage", true);
		}
		return "profile";
	}

	@GetMapping(value = "/image")
	@ResponseBody
	public byte[] getPoster(@RequestParam(name = "userId") Long userId, Model model)
			throws FileNotFoundException, IOException {
		File image = new File(userImages + userId);
		if (!image.exists() || !image.isFile()) {
			throw new FileNotFoundException("File Not Found");
		}
		System.out.println(userId+"'s Image");
		return IOUtils.toByteArray(new FileInputStream(image));
	}

	@GetMapping(value = "/edit")
	public String editUser(@RequestParam(value = "userId") Long userId, Model model) {
		UserApp userApp = userAppService.getUser(userId);
		model.addAttribute("userApp", userApp);
		if (genders.contains(userApp.getGender())) {
			genders.remove(userApp.getGender());
		}
		model.addAttribute("genders", genders);
		model.addAttribute("genderSaved", userApp.getGender());
		return "editUser";
	}

	@PostMapping(value = "/update")
	public String updateUser(@Valid UserApp u, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("genders", genders);
		model.addAttribute("genderSaved", u.getGender());
		if (genders.contains(u.getGender())) {
			genders.remove(u.getGender());
		}
		if (bindingResult.hasErrors()) {
			return "editUser";
		} else if (userAppService.checkIfUsernameExists(u.getUsername())) {
			model.addAttribute("duplicateUsername", true);
			return "editUser";
		} else if (userAppService.checkIfEmailExists(u.getEmail())) {
			model.addAttribute("duplicateEmail", true);
			return "editUser";
		} else
			userAppService.updateUser(u);
		redirectAttributes.addFlashAttribute("userUpdated", true);
		redirectAttributes.addFlashAttribute("u", userAppService.getUser(u.getUserId()));
		return "redirect:/user/profile";
	}

	@GetMapping(value = "/changepassword")
	public String changePassword(Model model) {
		model.addAttribute("changePassword", new ChangePassword());
		return "changePassword";
	}

	@PostMapping(value = "/change")
	public String changePassword(@Valid ChangePassword changePwd, BindingResult bindingResult, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		UserApp u = userAppService.findUserByUsername(username);
		if (bindingResult.hasErrors()) {
			return "changePassword";
		} else if (!passwordEncoder.matches(changePwd.getCurrentPassword(), u.getPassword())) {
			model.addAttribute("currentPasswordError", true);
			return "changePassword";
		} else if (!changePwd.getNewPassword().equals(changePwd.getConfirmPassword())) {
			model.addAttribute("confirmPasswordError", true);
			return "changePassword";
		}
		userAppService.changePassword(u, changePwd.getNewPassword());
		return "redirect:/user/profile";
	}

	@PostMapping(value = "/upload")
	public String uploadUserImage(@RequestParam(name = "file") MultipartFile file)
			throws IllegalStateException, IOException {
		userAppService.uploadUserImage(file);
		return "redirect:/user/profile";
	}
}

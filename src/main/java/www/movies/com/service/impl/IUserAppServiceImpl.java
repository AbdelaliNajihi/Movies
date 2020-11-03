package www.movies.com.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import www.movies.com.dao.IRoleRepository;
import www.movies.com.dao.IUserAppRepository;
import www.movies.com.model.Role;
import www.movies.com.model.UserApp;
import www.movies.com.service.IUserAppService;

@Service
@Transactional
@RequiredArgsConstructor
public class IUserAppServiceImpl implements IUserAppService {
	private final IUserAppRepository userAppRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Value("${userImages}")
	private String userImages;
	private final IRoleRepository roleRepository;

	@Override
	public void saveUser(UserApp u, MultipartFile file) throws IllegalStateException, IOException {
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		File userImagesDirectory = new File(userImages);
		if (!userImagesDirectory.exists()) {
			userImagesDirectory.mkdirs();
		}
		if (file != null && !file.isEmpty()) {
			u.setImage(file.getOriginalFilename());
			userAppRepository.save(u);
			file.transferTo(new File(userImages + u.getUserId()));
		}
		userAppRepository.save(u);
		addRoleToUser(u.getUserId(), "USER");
	}

	@Override
	public boolean checkIfUsernameExists(String username) {
		if (userAppRepository.findByUsername(username) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkIfEmailExists(String email) {
		if (userAppRepository.findByEmail(email) != null) {
			return true;
		}
		return false;
	}

	@Override
	public UserApp getUser(Long userId) {
		return userAppRepository.getOne(userId);
	}

	@Override
	public UserApp getLoggedinUser(Authentication authentication) {
		return userAppRepository.findByUsername(authentication.getName());
	}

	@Override
	public UserApp addRoleToUser(Long userId, String roleName) {
		UserApp userApp = getUser(userId);
		Role role = roleRepository.findByRoleName(roleName);
		userApp.getRoles().add(role);
		return userAppRepository.save(userApp);
	}

	@Override
	public void updateUser(UserApp u) {
		UserApp userApp = userAppRepository.getOne(u.getUserId());
		userApp.setUsername(u.getUsername());
		userApp.setFirstName(u.getFirstName());
		userApp.setLastName(u.getLastName());
		userApp.setEmail(u.getEmail());
		userApp.setPhoneNumber(u.getPhoneNumber());
		userApp.setGender(u.getGender());
		userApp.setCountry(u.getCountry());
		userApp.setImage(userApp.getImage());
		userAppRepository.save(u);
	}

	@Override
	public void changePassword(UserApp u, String newPassword) {
		String encryptedPassword = passwordEncoder.encode(newPassword);
		u.setPassword(encryptedPassword);
		userAppRepository.save(u);
	}

	@Override
	public UserApp findUserByUsername(String username) {
		return userAppRepository.findByUsername(username);
	}

	@Override
	public List<UserApp> getAllUsers() {
		return userAppRepository.findAll();
	}

	@Override
	public void uploadUserImage(MultipartFile file) throws IllegalStateException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		UserApp userApp = findUserByUsername(username);
		File userImagesDirectory = new File(userImages);
		if (!userImagesDirectory.exists()) {
			userImagesDirectory.mkdirs();
		}
		if (file != null && !file.isEmpty()) {
			userApp.setImage(file.getOriginalFilename());
			userAppRepository.save(userApp);
			//file.transferTo(new File(userImages + userApp.getUsername()));
			Files.write(Paths.get(userImages + userApp.getUserId()), file.getBytes());
		}
	}

}

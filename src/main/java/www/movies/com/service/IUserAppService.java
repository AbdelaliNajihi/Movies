package www.movies.com.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import www.movies.com.model.UserApp;

public interface IUserAppService {
	void saveUser(UserApp u, MultipartFile file) throws IllegalStateException, IOException;
	boolean checkIfUsernameExists(String username);
	boolean checkIfEmailExists(String email);
	UserApp getLoggedinUser(Authentication authentication);
	UserApp addRoleToUser(Long userId, String roleName);
	void updateUser(UserApp u);
	void changePassword(UserApp u, String password);
	UserApp findUserByUsername(String username);
	List<UserApp> getAllUsers();
	UserApp getUser(Long userId);
	void uploadUserImage(MultipartFile file) throws IllegalStateException, IOException;
}

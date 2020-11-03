package www.movies.com;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;
import www.movies.com.dao.IRoleRepository;
import www.movies.com.model.Role;
import www.movies.com.model.UserApp;
import www.movies.com.service.IUserAppService;

@SpringBootApplication
@RequiredArgsConstructor
public class MoviesApplication implements ApplicationRunner {
	private final IRoleRepository roleRepository;
	private final IUserAppService userAppService;

	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		roleRepository.saveAll(Arrays.asList(new Role("ADMIN"), new Role("USER")));
		userAppService.saveUser(new UserApp(1L, "admin", "admin", "firstName", "lastName", "email@email.com",
				"phoneNumber", "male", "country", "image", Collections.EMPTY_SET, Collections.EMPTY_SET), null);
	}
}

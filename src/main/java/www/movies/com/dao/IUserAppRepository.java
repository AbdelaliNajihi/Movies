package www.movies.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import www.movies.com.model.UserApp;

@Repository
public interface IUserAppRepository extends JpaRepository<UserApp, Long> {
	UserApp findByUsername(String username);
	UserApp findByEmail(String email);
}

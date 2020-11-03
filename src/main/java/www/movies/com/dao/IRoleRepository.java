package www.movies.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import www.movies.com.model.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String roleName);
}

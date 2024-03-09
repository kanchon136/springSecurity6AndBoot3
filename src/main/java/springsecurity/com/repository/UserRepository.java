package springsecurity.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springsecurity.com.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findUserByUserName(String username);

}

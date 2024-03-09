package springsecurity.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springsecurity.com.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}

package springsecurity.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springsecurity.com.entity.Test;

public interface TestRepo  extends JpaRepository<Test, Long>{

}

package springsecurity.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import springsecurity.com.entity.Test;
import springsecurity.com.repository.TestRepo;

@Service

public class TestService {
	@Autowired
	private TestRepo repo;
	
	@Transactional
	public Test saveTest(Test test) {
		return repo.save(test);
	}
	
	
	

}

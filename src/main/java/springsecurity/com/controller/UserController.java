package springsecurity.com.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springsecurity.com.entity.User;
import springsecurity.com.repository.RoleRepository;
import springsecurity.com.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
@Autowired
private UserRepository userRepo;

@Autowired
private RoleRepository roleRepo;

@Autowired
private PasswordEncoder passwordEncoder;

@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/test")	
public ResponseEntity<?> testing(){
		return new ResponseEntity<>("Success the Login",HttpStatus.OK);
}

@PostMapping("/ceateUser")
public User saveUser(@RequestBody User user){
	if(!user.getRoles().isEmpty()) {
		user.getRoles().forEach(role-> {
		roleRepo.save(role);
		});
	}
	
	if(user.getPassword() != null) {
	var passwor=	passwordEncoder.encode(user.getPassword());
	user.setPassword(passwor);
	}
	return userRepo.save(user);
}
}

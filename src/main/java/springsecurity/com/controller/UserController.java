package springsecurity.com.controller;
 
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springsecurity.com.customException.JwtExpiredException;
import springsecurity.com.dto.UserRequest;
import springsecurity.com.dto.UserResponse;
import springsecurity.com.entity.User;
import springsecurity.com.repository.RoleRepository;
import springsecurity.com.repository.UserRepository;
import springsecurity.com.util.JWTUtil;

import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {
	
@Autowired
private UserRepository userRepo;

@Autowired
private RoleRepository roleRepo;

@Autowired
private PasswordEncoder passwordEncoder;

@Autowired
private AuthenticationManager authenticationManager;

@Autowired
private UserDetailsService userDetailsService;

@Autowired
private JWTUtil jwtUtil;

@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/test")	
public ResponseEntity<?> testing(){
		var randonint = String.valueOf(new Random().nextInt(10*1000));
		System.out.println("=====randonint======"+randonint);
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

	@PostMapping("/authenticate")
	public ResponseEntity<UserResponse> userLoginAndGetToken(@RequestBody UserRequest userRequest) {
		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userRequest.getUserName());
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					userRequest.getUserName(), userRequest.getPassword()));

			if (authentication.isAuthenticated()) {
				var token = jwtUtil.generateToken(userRequest.getUserName());
				return ResponseEntity.ok(new UserResponse(token, "Token Generated Successfully", jwtUtil.getClaims(token).getExpiration()));
			} else {
				return new ResponseEntity<UserResponse>(new UserResponse(null, null, null), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			}
		}catch (ExpiredJwtException ex) {
			 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, "Token Expired", null));
		} catch (Exception e) {
			 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponse(null, "Internal Server Error", null));
		}
	}
}

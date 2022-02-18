package com.learning.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import com.learning.dto.User;
import com.learning.exception.IdNotFoundException;
import com.learning.service.UserService;
import com.learning.dto.EROLE;
import com.learning.dto.Role;
import com.learning.payload.request.LoginRequest;
import com.learning.payload.request.SignupRequest;
import com.learning.payload.response.JwtResponse;
import com.learning.payload.response.MessageResponse;
import com.learning.repository.RoleRepository;
import com.learning.repository.UserRepository;
import com.learning.security.jwt.JwtUtils;
import com.learning.security.services.UserDetailsImpl;
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService userservice;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/authenticate")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
						loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateToken(authentication);
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetailsImpl.getAuthorities()
				.stream()
				.map(i->i.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetailsImpl.getId(),
				userDetailsImpl.getUsername(),
				userDetailsImpl.getEmail(),
				roles,
				userDetailsImpl.getAddress()));
	}
	
	@PostMapping("/auth/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		
		User user = new User(signupRequest.getUsername(),
				signupRequest.getEmail(),
				signupRequest.getName(),
				passwordEncoder.encode(signupRequest.getPassword()),signupRequest.getAddress());
		
		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles==null) {
			Role userRole = roleRepository.findByRoleName(EROLE.ROLE_USER)
					.orElseThrow(()-> new RuntimeException("Error: role not found"));
			roles.add(userRole);
		}
		else {
			strRoles.forEach(e->{
				switch (e) {
				case "admin":
					Role roleAdmin = roleRepository.findByRoleName(EROLE.ROLE_ADMIN)
							.orElseThrow(()-> new RuntimeException("Error: role not found"));
					roles.add(roleAdmin);
					break;
				default:
					Role roleUser = roleRepository.findByRoleName(EROLE.ROLE_USER)
							.orElseThrow(()-> new RuntimeException("Error: role not found"));
					roles.add(roleUser);
					break;
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity
				.status(201)
				.body(new MessageResponse("User created successfully"));
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@Valid @PathVariable("id") Long id) throws IdNotFoundException{
		User user = userservice.getUserById(id);
		if(user == null) {
			throw new IdNotFoundException("Id not found exception.");
		}
		else {
			return ResponseEntity.status(201).body(user);
		}
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUserById(@Valid @PathVariable("id") Long id,@RequestBody User user) throws IdNotFoundException{
		User user2;
		try {
			user2 = userservice.updateUserById(id,user);
			return ResponseEntity.status(201).body(user2);
		} catch (IdNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Id not Found.");
		}
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUserById(@Valid @PathVariable("id") Long id) throws IdNotFoundException{
		 boolean isRemoved = userservice.deleteUserById(id);

	        if (isRemoved) {
	            return ResponseEntity.status(200).body("User deleted Successfully");
	        }

	        return ResponseEntity.badRequest().body("User with Id: "+id+" Not Found.");
	}

}

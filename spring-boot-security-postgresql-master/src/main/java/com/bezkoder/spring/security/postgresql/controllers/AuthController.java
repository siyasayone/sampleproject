package com.bezkoder.spring.security.postgresql.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.postgresql.dto.loginCredentialsDTO.LoginCredentialsDTO;
import com.bezkoder.spring.security.postgresql.dto.passwordTokenDTO.PasswordTokenDTO;
import com.bezkoder.spring.security.postgresql.dto.userDto.UserDTO;
import com.bezkoder.spring.security.postgresql.dto.userDto.UserLoginDTO;
import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.LoginRequest;
import com.bezkoder.spring.security.postgresql.payload.request.SignupRequest;
import com.bezkoder.spring.security.postgresql.payload.response.JwtResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import com.bezkoder.spring.security.postgresql.security.services.UserRegistrationService;
import com.bezkoder.spring.security.postgresql.security.services.userPassword.UserPasswordService;
import com.bezkoder.spring.security.postgresql.util.mailAndpdf.GenereateResetforgotPasswordLink;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/socialmedia/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRegistrationService userRegistrationService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private UserPasswordService userPasswordService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		UserLoginDTO dto = new UserLoginDTO();
		dto.setId(userDetails.getId());
		dto.setUserName(userDetails.getUsername());
		dto.setToken(jwt);
		dto.setStatus("Y");
		saveUserLogin(dto);

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@GetMapping("/token")
	public UserLoginDTO getDetailsByToken(@RequestParam String token) {
		return userRegistrationService.getDetailsByToken(token);
	}

	@PostMapping("/saveUserLogin")
	public UserLoginDTO saveUserLogin(@RequestBody UserLoginDTO userLoginDTO) {
		return userRegistrationService.saveUserLogin(userLoginDTO.getId(), userLoginDTO.getToken(),
				userLoginDTO.getStatus(), userLoginDTO.getUserName());
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		if (signUpRequest.getGender() != null) {
			if (signUpRequest.getGender().equalsIgnoreCase("")) {
				return ResponseEntity.badRequest().body(
						new MessageResponse("Please enter any of these values as Gender Type 'Male','Female','Other'"));
			} else if (!signUpRequest.getGender().toLowerCase().equalsIgnoreCase("female")
					&& !signUpRequest.getGender().toLowerCase().equalsIgnoreCase("male")
					&& !signUpRequest.getGender().toLowerCase().equalsIgnoreCase("other")) {
				return ResponseEntity.badRequest().body(
						new MessageResponse("Please enter any of these values as Gender Type 'Male','Female','Other'"));
			}
		}

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		format.setLenient(false);

		String date = signUpRequest.getDob();
		if (signUpRequest.getDob() != null && signUpRequest.getDob() != "") {
			try {
				format.parse(date);
				Date date1 = format.parse(date);
				signUpRequest.setDateOfBirth(date1);

			} catch (ParseException e) {
				System.out.println("Date " + date + " is not valid according to " + format.toPattern() + " pattern.");
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Given date is not valid according to the pattern dd/mm/yyyy"));
			}
		}
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role role = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(role);
		} else {
			Role role = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(role);
		}

		ERole userRole = null;
		for (Role r : roles) {
			userRole = r.getName();
		}
		signUpRequest.setUserrole(userRole);
		User user = userRegistrationService.saveUser(signUpRequest);
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@GetMapping("/forgotPassword")
	public ResponseEntity<?> checkMailAndUser(@Valid @NotBlank @NotEmpty @RequestParam("email") String email) {
		if (email == "") {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter an EmailId"));
		}
		UserDTO dto = userPasswordService.checkMailAndUser(email);
		if (dto == null) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("We didn't find an account for that e-mail address."));
		} else {
			dto.setResetToken(UUID.randomUUID().toString());
			PasswordTokenDTO password = new PasswordTokenDTO();
			password.setId(dto.getUserId());
			password.setUserName(dto.getUserName());
			password.setName(dto.getName());
			password.setToken(dto.getResetToken());
			password.setStatus("Y");
			savePasswordToken(password);
			GenereateResetforgotPasswordLink.sendResetPasswordLink(dto, email, dto.getResetToken());
		}
		return ResponseEntity.ok(new MessageResponse("A password reset link has been sent to " + email));
	}

	@PostMapping("/savePasswordToken")
	public PasswordTokenDTO savePasswordToken(@RequestBody PasswordTokenDTO passwordTokenDTO) {
		return userRegistrationService.savePasswordToken(passwordTokenDTO);
	}

	@PostMapping("/updateResetPassword")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody LoginCredentialsDTO loginCredentialsDTO) {
		return userPasswordService.updatePassword(loginCredentialsDTO);
	}

}

package com.bezkoder.spring.security.postgresql.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.postgresql.payload.request.UpdateUser;
import com.bezkoder.spring.security.postgresql.payload.request.UserDetailsRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.services.UserRegistrationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/socialmedia/user")
@Slf4j
public class UserController {

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@GetMapping("getUserDetails")
	public ResponseEntity<UserDetailsRequest> getUserRegistrationDetails(
			@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replaceAll("Bearer ", "");
		UserDetailsRequest user = userRegistrationService.getUserRegistrationDetails(token);
		return new ResponseEntity<UserDetailsRequest>(user, HttpStatus.OK);
	}

	@GetMapping("ViewProfile")
	public ResponseEntity<UserDetailsRequest> getUserProfileView(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replaceAll("Bearer ", "");
		UserDetailsRequest user = userRegistrationService.getUserProfileView(token);
		return new ResponseEntity<UserDetailsRequest>(user, HttpStatus.OK);
	}

	@PostMapping("/updateUser")
	public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authHeader,
			@Valid @RequestBody UpdateUser updateUser) {

		if (updateUser.getDob() == null && updateUser.getEmail() == null && updateUser.getFirstName() == null
				&& updateUser.getGender() == null && updateUser.getSurName() == null
				&& updateUser.getUsername() == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter values to update this user"));
		}

		if (updateUser.getGender() != null) {
			if (updateUser.getGender().equalsIgnoreCase("")) {
				return ResponseEntity.badRequest().body(
						new MessageResponse("Please enter any of these values as Gender Type 'Male','Female','Other'"));
			} else if (!updateUser.getGender().toLowerCase().equalsIgnoreCase("female")
					&& !updateUser.getGender().toLowerCase().equalsIgnoreCase("male")
					&& !updateUser.getGender().toLowerCase().equalsIgnoreCase("other")) {
				return ResponseEntity.badRequest().body(
						new MessageResponse("Please enter any of these values as Gender Type 'Male','Female','Other'"));
			}
		}

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		format.setLenient(false);

		String date = updateUser.getDob();
		if (updateUser.getDob() != null && updateUser.getDob() != "") {
			try {
				format.parse(date);
				Date date1 = format.parse(date);
				updateUser.setDateOfBirth(date1);

			} catch (ParseException e) {
				System.out.println("Date " + date + " is not valid according to " + format.toPattern() + " pattern.");
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Given date is not valid according to the pattern dd/mm/yyyy"));
			}
		}

		updateUser.setToken(authHeader.replaceAll("Bearer ", ""));
		ResponseEntity<?> user = userRegistrationService.updateUser(updateUser);
		return user;
	}

	@GetMapping("/delete")
	public ResponseEntity<Object> deleteUserRegistrationDetails(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replaceAll("Bearer ", "");
		return userRegistrationService.deleteUserRegistrationDetails(token);

	}
}

package com.bezkoder.spring.security.postgresql.security.services.userPassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.security.postgresql.dto.loginCredentialsDTO.LoginCredentialsDTO;
import com.bezkoder.spring.security.postgresql.dto.userDto.UserDTO;
import com.bezkoder.spring.security.postgresql.entity.passwordToken.PasswordToken;
import com.bezkoder.spring.security.postgresql.exception.UnAuthorizedException;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.user.PasswordTokenRepository;
import com.bezkoder.spring.security.postgresql.repository.user.UserLoginHistoryRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Siya
 *
 */
@Service
@Slf4j
public class UserPasswordService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PasswordTokenRepository passwordTokenRepository;
	@Autowired
	private UserLoginHistoryRepository userLoginHistoryRepository;

	public UserDTO checkMailAndUser(String email) {
		// TODO Auto-generated method stub
		UserDTO dto = null;
		User entity = userRepository.checkMail(email);
		if (entity != null) {
			dto = new UserDTO();
			dto.setEmailId(entity.getEmail());
			dto.setName(entity.getFirstName() + " " + entity.getSurName());
			dto.setUserName(entity.getUsername());
			dto.setUserId(entity.getId());

		}
		return dto;
	}

	public ResponseEntity<?> updatePassword(LoginCredentialsDTO loginCredentialsDTO) {
		// TODO Auto-generated method stub
		PasswordToken login = passwordTokenRepository.findBytoken(loginCredentialsDTO.getToken());
		if (login == null) {
			throw new UnAuthorizedException("644", "Unauthorized User,Token Expired");
		}
		passwordTokenRepository.updatePasswordTokenUserId(login.getUserId());
		userLoginHistoryRepository.updateUserLoginUserId(login.getUserId());
		User entity = userRepository.findByUsernameIs(login.getUserName());
		if (entity == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: User Not Found!"));
		}
		entity.setPassword(passwordEncoder.encode(loginCredentialsDTO.getPassword()));
		User yr = userRepository.save(entity);
		return new ResponseEntity<>(yr, HttpStatus.CREATED);
	}

}

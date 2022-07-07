package com.bezkoder.spring.security.postgresql.security.services;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.bezkoder.spring.security.postgresql.dto.mail.MailDTO;
import com.bezkoder.spring.security.postgresql.dto.passwordTokenDTO.PasswordTokenDTO;
import com.bezkoder.spring.security.postgresql.dto.userDto.UserLoginDTO;
import com.bezkoder.spring.security.postgresql.entity.passwordToken.PasswordToken;
import com.bezkoder.spring.security.postgresql.entity.user.UserLoginHistory;
import com.bezkoder.spring.security.postgresql.exception.BusinessException;
import com.bezkoder.spring.security.postgresql.exception.UnAuthorizedException;
import com.bezkoder.spring.security.postgresql.exception.UserNotFoundException;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.SignupRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UpdateUser;
import com.bezkoder.spring.security.postgresql.payload.request.UserDetailsRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.user.PasswordTokenRepository;
import com.bezkoder.spring.security.postgresql.repository.user.UserLoginHistoryRepository;
import com.bezkoder.spring.security.postgresql.util.mailAndpdf.RegisteredUsersMail;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Siya
 *
 */
@Service
@Slf4j
public class UserRegistrationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private UserLoginHistoryRepository userLoginHistoryRepository;

	@Autowired
	private PasswordTokenRepository passwordTokenRepository;

	public User saveUser(SignupRequest signupRequest) {
		User entity = userRepository.findByUsernameIs(signupRequest.getUsername());
		Calendar cl = Calendar.getInstance();
		if (entity == null) {
			entity = new User();
			entity.setFirstName(signupRequest.getFirstName());
			entity.setSurName(signupRequest.getSurName());
			entity.setDateOfBirth(signupRequest.getDateOfBirth());
			entity.setGender(signupRequest.getGender());
			entity.setEmail(signupRequest.getEmail());
			entity.setUsername(signupRequest.getUsername());
			entity.setUserrole(signupRequest.getUserrole());
			entity.setPassword(encoder.encode(signupRequest.getPassword()));
			entity.setIsActive("Y");
			entity.setIsDeleted("N");
			entity.setCreatedDate(cl.getTime());
		} else {
			entity.setId(signupRequest.getUserRegistrationId());
			entity.setFirstName(signupRequest.getFirstName());
			entity.setSurName(signupRequest.getSurName());
			entity.setDateOfBirth(signupRequest.getDateOfBirth());
			entity.setGender(signupRequest.getGender());
			entity.setEmail(signupRequest.getEmail());
			entity.setUserrole(signupRequest.getUserrole());
			entity.setUsername(signupRequest.getUsername());
			entity.setPassword(encoder.encode(signupRequest.getPassword()));
			entity.setIsActive("Y");
			entity.setIsDeleted("N");
			entity.setModifiedDate(cl.getTime());
			entity.setModifiedBy(signupRequest.getFirstName() + " " + signupRequest.getSurName());
		}
		User yr = userRepository.save(entity);
		MailDTO mailDTO = new MailDTO();
		mailDTO.setUsermasterId(yr.getId());
		mailDTO.setMailId(signupRequest.getEmail());
		mailDTO.setName(signupRequest.getFirstName() + " " + signupRequest.getSurName());
		mailDTO.setUsername(signupRequest.getUsername());
		mailDTO.setPassword(signupRequest.getPassword());
		registeredUsers(mailDTO);
		return yr;
	}

	public void registeredUsers(@RequestBody MailDTO mailDTO) {
		RegisteredUsersMail.registeredUsers(mailDTO);
	}

	public UserDetailsRequest getUserRegistrationDetails(String token) {
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("634", "Unauthorized User,Token Expired");
		}
		try {
			UserDetailsRequest dto = new UserDetailsRequest();
			User yr = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
			if (yr == null) {
				throw new UserNotFoundException("620", "User Not Found");
			}
			if (yr != null) {
				dto.setUserRegistrationId(yr.getId());
				dto.setUsername(yr.getUsername());
				dto.setFirstName(yr.getFirstName());
				dto.setSurName(yr.getSurName());
				dto.setDateOfBirth(yr.getDateOfBirth());
				dto.setGender(yr.getGender());
				dto.setEmail(yr.getEmail());
				dto.setUserrole(yr.getUserrole());
			}
			return dto;
		} catch (IllegalArgumentException e) {
			throw new BusinessException("608", "Please enter a valid Id" + e.getMessage());
		}
	}

	public ResponseEntity<Object> deleteUserRegistrationDetails(String token) {
		Calendar cl = Calendar.getInstance();
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("645", "Unauthorized User,Token Expired");
		}
		User yr = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
		if (yr != null) {
			yr.setIsActive("N");
			yr.setIsDeleted("Y");
			yr.setModifiedDate(cl.getTime());
			userRepository.save(yr);
			return new ResponseEntity<>("User Deleted Successfully", HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: User Not Found!"));
		}
	}

	public UserDetailsRequest getUserProfileView(String token) {
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("646", "Unauthorized User,Token Expired");
		}
		try {
			UserDetailsRequest dto = new UserDetailsRequest();
			User entity = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
			if (entity != null) {
				dto.setUserRegistrationId(entity.getId());
				dto.setDateOfBirth(entity.getDateOfBirth());
				dto.setFirstName(entity.getFirstName());
				dto.setSurName(entity.getSurName());
				dto.setUsername(entity.getUsername());
				dto.setEmail(entity.getEmail());
				dto.setGender(entity.getGender());
				dto.setUserrole(entity.getUserrole());
			}
			return dto;
		} catch (IllegalArgumentException e) {
			throw new BusinessException("604", "Please enter a valid Id" + e.getMessage());
		}
	}

	public UserLoginDTO saveUserLogin(Long userId, String token, String status, String userName) {
		userLoginHistoryRepository.updateUserLoginUserId(userId);
		UserLoginHistory user = userLoginHistoryRepository.findByUserId(userId, token);
		if (user == null) {
			user = new UserLoginHistory();
			user.setLoginTime(Calendar.getInstance().getTime());
			user.setLastUpdatedTime(Calendar.getInstance().getTime());
			user.setToken(token);
			user.setUserName(userName);
			user.setUserId(userId);
			user.setOnlineStatus("Y");
		}
		userLoginHistoryRepository.save(user);
		return null;
	}

	public ResponseEntity<?> updateUser(UpdateUser updateUser) {
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(updateUser.getToken());
		if (login == null) {
			throw new UnAuthorizedException("647", "Unauthorized User,Token Expired");
		}
		User entity = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
		User username = userRepository.findByUsernameIs(updateUser.getUsername());
		User email = userRepository.findByEmailIs(updateUser.getEmail());
		if (username != null) {
			if (!username.getId().equals(entity.getId())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
			}
		}
		if (email != null) {
			if (!email.getId().equals(entity.getId())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
			}
		}
		Calendar cl = Calendar.getInstance();
		if (updateUser.getFirstName() != null && updateUser.getFirstName() != "") {
			entity.setFirstName(updateUser.getFirstName());
		}
		if (updateUser.getSurName() != null && updateUser.getSurName() != "") {
			entity.setSurName(updateUser.getSurName());
		}
		if (updateUser.getDateOfBirth() != null) {
			entity.setDateOfBirth(updateUser.getDateOfBirth());
		}
		if (updateUser.getGender() != null && updateUser.getGender() != "") {
			entity.setGender(updateUser.getGender());
		}
		if (updateUser.getEmail() != null && updateUser.getEmail() != "") {
			entity.setEmail(updateUser.getEmail());
		}
		if (updateUser.getUsername() != null && updateUser.getUsername() != "") {
			entity.setUsername(updateUser.getUsername());
		}
		entity.setIsActive("Y");
		entity.setIsDeleted("N");
		entity.setModifiedDate(cl.getTime());
		entity.setModifiedBy("Modified User");
		User yr = userRepository.save(entity);
		return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
	}

	public UserLoginDTO getDetailsByToken(String token) {
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("648", "Unauthorized User,Token Expired");
		}
		UserLoginDTO dto = new UserLoginDTO();
		dto.setId(login.getUserId());
		dto.setUserName(login.getUserName());
		dto.setStatus(login.getOnlineStatus());
		return dto;
	}

	public PasswordTokenDTO savePasswordToken(PasswordTokenDTO passwordTokenDTO) {
		passwordTokenRepository.updatePasswordTokenUserId(passwordTokenDTO.getId());
		PasswordToken user = passwordTokenRepository.findByUserId(passwordTokenDTO.getId(),
				passwordTokenDTO.getToken());
		if (user == null) {
			user = new PasswordToken();
			user.setLoginTime(Calendar.getInstance().getTime());
			user.setLastUpdatedTime(Calendar.getInstance().getTime());
			user.setToken(passwordTokenDTO.getToken());
			user.setUserName(passwordTokenDTO.getUserName());
			user.setUserId(passwordTokenDTO.getId());
			user.setOnlineStatus("Y");
		}
		passwordTokenRepository.save(user);
		return null;
	}

}
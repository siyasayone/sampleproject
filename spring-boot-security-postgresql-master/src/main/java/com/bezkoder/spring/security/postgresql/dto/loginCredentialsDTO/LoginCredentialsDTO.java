package com.bezkoder.spring.security.postgresql.dto.loginCredentialsDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentialsDTO {

	private String token;

	@NotEmpty
	@Size(min = 8, message = "password should have at least 8 characters")
	private String password;

}
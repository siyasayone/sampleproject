package com.bezkoder.spring.security.postgresql.dto.passwordTokenDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordTokenDTO {

	private Long id;

	private String token;

	private String status;

	private String userName;

	private String name;

}
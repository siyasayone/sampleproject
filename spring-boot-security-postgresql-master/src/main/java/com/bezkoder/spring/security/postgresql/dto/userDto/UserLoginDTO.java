package com.bezkoder.spring.security.postgresql.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

	private Long id;

	private String token;

	private String status;

	private String userName;

}

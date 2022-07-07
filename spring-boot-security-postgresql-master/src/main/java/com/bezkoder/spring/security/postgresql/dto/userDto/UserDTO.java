
package com.bezkoder.spring.security.postgresql.dto.userDto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;

	private String name;

	private String userName;

	private String emailId;

	private String resetToken;

}

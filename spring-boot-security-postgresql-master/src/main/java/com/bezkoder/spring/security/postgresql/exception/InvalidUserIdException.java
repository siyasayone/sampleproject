package com.bezkoder.spring.security.postgresql.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Siya
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvalidUserIdException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int errorcode;

	private String errorMessage;

}
package com.bezkoder.spring.security.postgresql.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class CommentCreationDTO {

	private Long commentId;

	@NotNull
	private Long postId;

	@NotEmpty
	@NotBlank
	private String comment;

	private String token;

}
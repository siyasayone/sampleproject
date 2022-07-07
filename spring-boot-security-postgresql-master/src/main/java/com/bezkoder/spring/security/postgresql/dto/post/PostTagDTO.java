package com.bezkoder.spring.security.postgresql.dto.post;

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
public class PostTagDTO {

	private Long tagPostsId;

	@NotNull
	private Long postId;

	@NotNull
	private Long taggedusersId;

}
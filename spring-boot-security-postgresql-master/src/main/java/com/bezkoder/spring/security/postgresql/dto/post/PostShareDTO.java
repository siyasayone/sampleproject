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
public class PostShareDTO {

	private Long sharePostsId;

	@NotNull
	private Long postId;

	@NotNull
	private Long sharedUserId;

}
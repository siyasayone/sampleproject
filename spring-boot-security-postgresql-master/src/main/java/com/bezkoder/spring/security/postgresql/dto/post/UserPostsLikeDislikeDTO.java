package com.bezkoder.spring.security.postgresql.dto.post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class UserPostsLikeDislikeDTO {

	private Long userLikeDislikeId;

	@NotNull
	private Long postId;

	private String token;

	@NotNull
	@Pattern(regexp = "^true$|^false$", message = "allowed input: true or false")
	private String likeOrDislike;

}
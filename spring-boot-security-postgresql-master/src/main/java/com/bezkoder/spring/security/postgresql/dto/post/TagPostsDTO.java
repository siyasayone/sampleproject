package com.bezkoder.spring.security.postgresql.dto.post;

import java.util.List;

import javax.validation.Valid;

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
public class TagPostsDTO {

	private Long userId;

	private String token;

	@Valid
	private List<PostTagDTO> postTagDTO;

}
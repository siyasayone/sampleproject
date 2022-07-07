package com.bezkoder.spring.security.postgresql.dto.post;

import org.springframework.web.multipart.MultipartFile;

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
public class PostCreationDTO {

	private Long postId;

	private MultipartFile file;

	private String name;

	private String type;

	private String caption;

	private String uri;

	private long size;

	private Long userId;
}
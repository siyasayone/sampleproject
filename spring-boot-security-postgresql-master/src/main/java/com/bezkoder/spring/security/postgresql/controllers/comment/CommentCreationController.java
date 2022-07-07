
package com.bezkoder.spring.security.postgresql.controllers.comment;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.postgresql.dto.comment.CommentCreationDTO;
import com.bezkoder.spring.security.postgresql.security.services.comment.CommentCreationService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Siya
 *
 */

@RestController
@RequestMapping("/socialmedia/comments")
@Slf4j
public class CommentCreationController {

	@Autowired
	private CommentCreationService commentCreationService;

	@PostMapping("/")
	public ResponseEntity<?> createComments(@RequestHeader("Authorization") String authHeader,
			@Valid @RequestBody CommentCreationDTO commentCreationDTO) {
		String token = authHeader.replaceAll("Bearer ", "");
		commentCreationDTO.setToken(token);
		return commentCreationService.createComments(commentCreationDTO);
	}

	@GetMapping("/list")
	public Collection<CommentCreationDTO> retrieveCommentList(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replaceAll("Bearer ", "");
		return commentCreationService.retrieveCommentList(token);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<Object> deleteComments(@RequestHeader("Authorization") String authHeader,
			@Valid @NotNull @PathVariable("id") Long id) {
		String token = authHeader.replaceAll("Bearer ", "");
		return commentCreationService.deleteComments(token, id);
	}

}

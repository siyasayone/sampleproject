
package com.bezkoder.spring.security.postgresql.controllers.post;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.security.postgresql.dto.post.PostCreationDTO;
import com.bezkoder.spring.security.postgresql.dto.post.SharePostsDTO;
import com.bezkoder.spring.security.postgresql.dto.post.TagPostsDTO;
import com.bezkoder.spring.security.postgresql.dto.post.UserPostsLikeDislikeDTO;
import com.bezkoder.spring.security.postgresql.entity.post.PostCreation;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.post.PostCreationRepository;
import com.bezkoder.spring.security.postgresql.security.services.post.PostCreationService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Siya
 *
 */

@RestController
@RequestMapping("/socialmedia/posts")
@Slf4j
public class PostCreationController {

	@Autowired
	private PostCreationService postCreationService;
	@Autowired
	private PostCreationRepository postCreationRepository;

	@PostMapping("/uploadPost")
	public ResponseEntity<MessageResponse> uploadFile(@RequestHeader("Authorization") String authHeader,
			@RequestParam("file") MultipartFile file, @RequestParam String caption, @RequestParam Long postId) {
		String message = "";
		String token = authHeader.replaceAll("Bearer ", "");
		if (postId != null) {
			Boolean post = postCreationRepository.existsByPostId(postId);
			if (post == false) {
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Error: please enter valid input,No data exist in  db with this given postId !"));
			}
		} else if (postId == null) {
			if (file.isEmpty()) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Please enter file to upload !"));
			}
		}
		try {
			return postCreationService.store(file, token, caption, postId);
		} catch (Exception e) {
			message = "Access denied,Could not update post: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}

	@GetMapping("/listFiles")
	public Collection<PostCreationDTO> retrievePostList(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replaceAll("Bearer ", "");
		return postCreationService.retrievePostList(token);
	}

	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
		PostCreation fileDB = postCreationService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<Object> deletePost(@RequestHeader("Authorization") String authHeader,
			@Valid @NotNull @NotBlank @PathVariable("id") Long id) {
		String token = authHeader.replaceAll("Bearer ", "");
		return postCreationService.deletePost(token, id);
	}

	@PostMapping("/likeDislikeSave")
	public ResponseEntity<?> likeDislikePosts(@RequestHeader("Authorization") String authHeader,
			@Valid @RequestBody UserPostsLikeDislikeDTO userPostsLikeDislikeDTO) {
		String token = authHeader.replaceAll("Bearer ", "");
		userPostsLikeDislikeDTO.setToken(token);
		return postCreationService.likeDislikePosts(userPostsLikeDislikeDTO);
	}

	@PostMapping("/tagPost")
	public ResponseEntity<?> tagPosts(@RequestHeader("Authorization") String authHeader,
			@Valid @RequestBody TagPostsDTO tagPostsDTO) {
		String token = authHeader.replaceAll("Bearer ", "");
		tagPostsDTO.setToken(token);
		return postCreationService.tagPosts(tagPostsDTO);
	}

	@PostMapping("/sharePost")
	public ResponseEntity<?> sharePosts(@RequestHeader("Authorization") String authHeader,
			@Valid @RequestBody SharePostsDTO sharePostsDTO) {
		String token = authHeader.replaceAll("Bearer ", "");
		sharePostsDTO.setToken(token);
		return postCreationService.sharePosts(sharePostsDTO);
	}

}

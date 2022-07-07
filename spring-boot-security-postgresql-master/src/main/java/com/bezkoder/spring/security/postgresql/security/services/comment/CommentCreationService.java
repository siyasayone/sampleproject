package com.bezkoder.spring.security.postgresql.security.services.comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.security.postgresql.dto.comment.CommentCreationDTO;
import com.bezkoder.spring.security.postgresql.entity.comment.CommentCreation;
import com.bezkoder.spring.security.postgresql.entity.post.PostCreation;
import com.bezkoder.spring.security.postgresql.entity.user.UserLoginHistory;
import com.bezkoder.spring.security.postgresql.exception.NoCommentsListException;
import com.bezkoder.spring.security.postgresql.exception.UnAuthorizedException;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.comment.CommentCreationRepository;
import com.bezkoder.spring.security.postgresql.repository.post.PostCreationRepository;
import com.bezkoder.spring.security.postgresql.repository.user.UserLoginHistoryRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Siya
 *
 */
@Service
@Slf4j
public class CommentCreationService {

	@Autowired
	private CommentCreationRepository commentCreationRepository;

	@Autowired
	private UserLoginHistoryRepository userLoginHistoryRepository;

	@Autowired
	private PostCreationRepository postCreationRepository;

	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("unused")
	public ResponseEntity<?> createComments(CommentCreationDTO commentCreationDTO) {
		CommentCreation entity = commentCreationRepository.findByCommentId(commentCreationDTO.getCommentId());
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(commentCreationDTO.getToken());
		if (login == null) {
			throw new UnAuthorizedException("635", "Unauthorized User");
		}
		PostCreation test = new PostCreation();
		User user = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
		String post = postCreationRepository.findByPostId(commentCreationDTO.getPostId());
		if (post == null) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: There is no Post  in db with this Id,please enter valid input!"));
		}
		if (entity != null) {
			if (!entity.getUserId().equals(login.getUserId())) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Error: Access Denied,You don't have access to delete this data!"));
			}
		}
		Calendar cl = Calendar.getInstance();
		if (entity == null) {
			entity = new CommentCreation();
			entity.setUserId(login.getUserId());
			entity.setComment(commentCreationDTO.getComment());
			entity.setPostId(commentCreationDTO.getPostId());
			entity.setIsDeleted("N");
			entity.setCreatedDate(cl.getTime());
			entity.setCommentedBy(user.getFirstName() + " " + user.getSurName());
		} else {
			entity.setCommentId(commentCreationDTO.getCommentId());
			entity.setUserId(login.getUserId());
			entity.setPostId(commentCreationDTO.getPostId());
			entity.setComment(commentCreationDTO.getComment());
			entity.setIsDeleted("N");
			entity.setModifiedDate(cl.getTime());
		}
		CommentCreation yr = commentCreationRepository.save(entity);
		return new ResponseEntity<>(yr, HttpStatus.CREATED);
	}

	@SuppressWarnings("unused")
	public ResponseEntity<Object> deleteComments(String token, Long id) {
		Calendar cl = Calendar.getInstance();
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("636", "Unauthorized User");
		}
		CommentCreation yr = commentCreationRepository.findByCommentId(id);
		if (!yr.getUserId().equals(login.getUserId())) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Access Denied,You don't have access to delete this data!"));
		}
		if (yr != null) {
			yr.setIsDeleted("Y");
			yr.setModifiedDate(cl.getTime());
			commentCreationRepository.save(yr);
			return new ResponseEntity<>("Comment Deleted Successfully", HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: No Data!"));
		}
	}

	public Collection<CommentCreationDTO> retrieveCommentList(String token) {
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("637", "Unauthorized User");
		}
		List<CommentCreation> yr = commentCreationRepository.listByCommentId(login.getUserId());
		if (yr == null) {
			throw new NoCommentsListException(630, "This user doesn't posted any comments yet");
		}
		ArrayList<CommentCreationDTO> dtos = new ArrayList<CommentCreationDTO>();
		for (CommentCreation eachdto : yr) {
			CommentCreationDTO dto = new CommentCreationDTO();
			dto.setCommentId(eachdto.getCommentId());
			dto.setComment(eachdto.getComment());
			dto.setPostId(eachdto.getPostId());
			dtos.add(dto);
		}
		return dtos;
	}

}
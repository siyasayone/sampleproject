package com.bezkoder.spring.security.postgresql.security.services.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bezkoder.spring.security.postgresql.dto.post.PostCreationDTO;
import com.bezkoder.spring.security.postgresql.dto.post.PostShareDTO;
import com.bezkoder.spring.security.postgresql.dto.post.PostTagDTO;
import com.bezkoder.spring.security.postgresql.dto.post.SharePostsDTO;
import com.bezkoder.spring.security.postgresql.dto.post.TagPostsDTO;
import com.bezkoder.spring.security.postgresql.dto.post.UserPostsLikeDislikeDTO;
import com.bezkoder.spring.security.postgresql.entity.comment.CommentCreation;
import com.bezkoder.spring.security.postgresql.entity.post.PostCreation;
import com.bezkoder.spring.security.postgresql.entity.post.SharePosts;
import com.bezkoder.spring.security.postgresql.entity.post.TagPosts;
import com.bezkoder.spring.security.postgresql.entity.post.UserPostLikeDislike;
import com.bezkoder.spring.security.postgresql.entity.user.UserLoginHistory;
import com.bezkoder.spring.security.postgresql.exception.UnAuthorizedException;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.comment.CommentCreationRepository;
import com.bezkoder.spring.security.postgresql.repository.post.PostCreationRepository;
import com.bezkoder.spring.security.postgresql.repository.post.SharePostsRepository;
import com.bezkoder.spring.security.postgresql.repository.post.TagPostsRepository;
import com.bezkoder.spring.security.postgresql.repository.post.UserPostsLikeDislikeRepository;
import com.bezkoder.spring.security.postgresql.repository.user.UserLoginHistoryRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Siya
 *
 */
@Service
@Slf4j
public class PostCreationService {

	@Autowired
	private PostCreationRepository postCreationRepository;
	@Autowired
	private UserPostsLikeDislikeRepository userPostsLikeDislikeRepository;
	@Autowired
	private TagPostsRepository tagPostsRepository;
	@Autowired
	private SharePostsRepository sharePostsRepository;
	@Autowired
	private UserLoginHistoryRepository userLoginHistoryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentCreationRepository commentCreationRepository;

	public ResponseEntity<MessageResponse> store(MultipartFile file, String token, String caption, Long postId)
			throws IOException {
		String fileName = "";
		String message = "";
		if (file != null) {
			fileName = StringUtils.cleanPath(file.getOriginalFilename());
		}
		Boolean post = postCreationRepository.existsByPostId(postId);
		PostCreation test = new PostCreation();
		if (postId != null) {
			test = postCreationRepository.findById(postId).get();

		}

		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("638", "Unauthorized User,token Expired");
		}
		if (test.getUserId() != null) {
			if (!login.getUserId().equals(test.getUserId())) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Error: Access Denied,You don't have access to delete this data!"));

			}
		}

		User user = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
		Calendar cl = Calendar.getInstance();
		PostCreation entity = new PostCreation();
		PostCreation yr = new PostCreation();
		if (post == false) {
			entity.setUserId(login.getUserId());
			entity.setName(fileName);
			entity.setType(file.getContentType());
			entity.setData(file.getBytes());
			entity.setCaption(caption);
			entity.setIsDeleted("N");
			entity.setCreatedDate(cl.getTime());
			yr = postCreationRepository.save(entity);
		} else {
			entity.setPostId(postId);
			entity.setCaption(caption);
			if (file.getSize() > 0) {
				entity.setName(fileName);
				entity.setType(file.getContentType());
				entity.setData(file.getBytes());
			} else {
				entity.setName(test.getName());
				entity.setType(test.getType());
				entity.setData(test.getData());
				entity.setCreatedDate(test.getCreatedDate());
			}
			if (caption != null && caption != "") {
				entity.setCaption(caption);
			}
			entity.setUserId(login.getUserId());
			entity.setIsDeleted("N");
			entity.setModifiedDate(cl.getTime());
			entity.setModifiedBy(user.getFirstName() + " " + user.getSurName());
			yr = postCreationRepository.save(entity);
		}

		if (postId == null) {
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
		} else {
			message = "Updated successfully: " + file.getOriginalFilename();
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));

	}

	public PostCreation getFile(Long id) {

		return postCreationRepository.findById(id).get();
	}

	public ResponseEntity<Object> deletePost(String token, Long id) {
		Calendar cl = Calendar.getInstance();
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("639", "Unauthorized User,Invalid token");
		}
		String test = postCreationRepository.findByPostId(id);
		if (test == null) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: There is no Post  in db with this Id,please enter valid input!"));
		}
		PostCreation yr = postCreationRepository.findById(id).get();
		List<CommentCreation> comments = commentCreationRepository.findBypostId(id);
		List<UserPostLikeDislike> userlike = userPostsLikeDislikeRepository.findBypostId(id);
		List<SharePosts> share = sharePostsRepository.findBypostId(id);
		List<TagPosts> tag = tagPostsRepository.findBypostId(id);
		if (yr != null) {
			if (!yr.getUserId().equals(login.getUserId())) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Error: Access Denied,You don't have access to delete this data!"));
			}
			if (comments != null) {
				for (CommentCreation c : comments) {
					c.setIsDeleted("Y");
					c.setModifiedDate(cl.getTime());
					commentCreationRepository.save(c);
				}
			}
			if (userlike != null) {
				for (UserPostLikeDislike u : userlike) {
					u.setIsDeleted("Y");
					u.setModifiedDate(cl.getTime());
					userPostsLikeDislikeRepository.save(u);
				}
			}
			if (share != null) {
				for (SharePosts s : share) {
					s.setIsDeleted("Y");
					s.setModifiedDate(cl.getTime());
					sharePostsRepository.save(s);
				}

			}
			if (tag != null) {
				for (TagPosts t : tag) {
					t.setIsDeleted("Y");
					t.setModifiedDate(cl.getTime());
					tagPostsRepository.save(t);
				}
			}
			yr.setIsDeleted("Y");
			yr.setModifiedDate(cl.getTime());
			postCreationRepository.save(yr);
		}
		return new ResponseEntity<>("Post Deleted Successfully", HttpStatus.OK);
	}

	public ResponseEntity<?> likeDislikePosts(UserPostsLikeDislikeDTO userPostsLikeDislikeDTO) {
		// TODO Auto-generated method stub
		UserPostLikeDislike entity = userPostsLikeDislikeRepository
				.findByUserLikeDislikeId(userPostsLikeDislikeDTO.getUserLikeDislikeId());
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(userPostsLikeDislikeDTO.getToken());
		if (login == null) {
			throw new UnAuthorizedException("640", "Unauthorized User");
		}
		User user = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
		String test = postCreationRepository.findByPostId(userPostsLikeDislikeDTO.getPostId());
		if (test == null) {
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
			entity = new UserPostLikeDislike();
			entity.setPostId(userPostsLikeDislikeDTO.getPostId());
			entity.setUserId(login.getUserId());
			entity.setLikeOrDislike(userPostsLikeDislikeDTO.getLikeOrDislike());
			entity.setIsDeleted("N");
			entity.setCreatedDate(cl.getTime());
			entity.setModifiedBy(user.getFirstName() + " " + user.getSurName());
		} else {
			entity.setUserLikeDislikeId(userPostsLikeDislikeDTO.getUserLikeDislikeId());
			entity.setPostId(userPostsLikeDislikeDTO.getPostId());
			entity.setUserId(login.getUserId());
			entity.setLikeOrDislike(userPostsLikeDislikeDTO.getLikeOrDislike());
			entity.setIsDeleted("N");
			entity.setModifiedDate(cl.getTime());
		}
		UserPostLikeDislike yr = userPostsLikeDislikeRepository.save(entity);
		return new ResponseEntity<>(yr, HttpStatus.CREATED);
	}

	public ResponseEntity<?> tagPosts(TagPostsDTO tagPostsDTO) {
		// TODO Auto-generated method stub
		Calendar cl = Calendar.getInstance();
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(tagPostsDTO.getToken());
		if (login == null) {
			throw new UnAuthorizedException("641", "Unauthorized User");
		}
		User user = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
		if (tagPostsDTO.getPostTagDTO() == null || tagPostsDTO.getPostTagDTO().size() == 0) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Please enter valid inputs,input field is  empty!"));
		}
		TagPosts js = new TagPosts();
		for (@Valid
		PostTagDTO dto : tagPostsDTO.getPostTagDTO()) {
			TagPosts tagPosts = tagPostsRepository.findByTagPostsId(dto.getTagPostsId());
			String test = postCreationRepository.findByPostId(dto.getPostId());
			if (test == null) {
				return ResponseEntity.badRequest().body(
						new MessageResponse("Error: There is no Post  in db with this Id,please enter valid input!"));
			}
			User userId = userRepository.findByUserRegistrationIdAndIsDeleted(dto.getTaggedusersId());
			if (userId == null) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Error: This userId doesn't exist,please enter valid inputs!"));
			}
			if (tagPosts == null) {
				tagPosts = new TagPosts();
				tagPosts.setTagPostsId(0L);
				tagPosts.setCreatedDate(cl.getTime());
				tagPosts.setPostId(dto.getPostId());
				tagPosts.setUserId(login.getUserId());
				tagPosts.setTaggedUsersId(dto.getTaggedusersId());
				tagPosts.setIsDeleted("N");
				tagPosts.setModifiedBy(user.getFirstName() + " " + user.getSurName());
			}
			js = tagPostsRepository.save(tagPosts);
		}
		return new ResponseEntity<>(js, HttpStatus.CREATED);
	}

	public ResponseEntity<?> sharePosts(SharePostsDTO sharePostsDTO) {
		// TODO Auto-generated method stub
		Calendar cl = Calendar.getInstance();
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(sharePostsDTO.getToken());
		if (login == null) {
			throw new UnAuthorizedException("642", "Unauthorized User");
		}
		User user = userRepository.findByUserRegistrationIdAndIsDeleted(login.getUserId());
		if (sharePostsDTO.getPostShareDTO() == null || sharePostsDTO.getPostShareDTO().size() == 0) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Please enter valid inputs,input field is  empty!"));
		}
		SharePosts js = new SharePosts();
		for (@Valid
		PostShareDTO dto : sharePostsDTO.getPostShareDTO()) {
			SharePosts sharePosts = sharePostsRepository.findBySharePostsId(dto.getSharePostsId());
			String test = postCreationRepository.findByPostId(dto.getPostId());
			if (test == null) {
				return ResponseEntity.badRequest().body(
						new MessageResponse("Error: There is no Post  in db with this Id,please enter valid input!"));
			}
			User userId = userRepository.findByUserRegistrationIdAndIsDeleted(dto.getSharedUserId());
			if (userId == null) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Error: This userId doesn't exist,please enter valid inputs!"));
			}
			if (sharePosts == null) {
				sharePosts = new SharePosts();
				sharePosts.setSharePostsId(0L);
				sharePosts.setCreatedDate(cl.getTime());
				sharePosts.setPostId(dto.getPostId());
				sharePosts.setUserId(login.getUserId());
				sharePosts.setSharePostsId(dto.getSharedUserId());
				sharePosts.setIsDeleted("N");
				sharePosts.setModifiedBy(user.getFirstName() + " " + user.getSurName());
			}
			js = sharePostsRepository.save(sharePosts);
		}
		return new ResponseEntity<>(js, HttpStatus.CREATED);
	}

	public Collection<PostCreationDTO> retrievePostList(String token) {
		UserLoginHistory login = userLoginHistoryRepository.findBytoken(token);
		if (login == null) {
			throw new UnAuthorizedException("643", "Unauthorized User,Invalid token");
		}
		List<PostCreation> yr = postCreationRepository.listByPostId(login.getUserId());
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").toUriString();
		ArrayList<PostCreationDTO> dtos = new ArrayList<PostCreationDTO>();
		for (PostCreation eachdto : yr) {
			PostCreationDTO dto = new PostCreationDTO();
			dto.setPostId(eachdto.getPostId());
			if (eachdto.getName() != null) {
				dto.setName(eachdto.getName());
			}
			if (eachdto.getType() != null) {
				dto.setType(eachdto.getType());
			}
			if (fileDownloadUri != null) {
				dto.setUri(fileDownloadUri);
			}
			if (eachdto.getCaption() != null) {
				dto.setCaption(eachdto.getCaption());
			}
			dto.setUserId(eachdto.getUserId());
			if (eachdto.getData() != null) {
				dto.setSize(eachdto.getData().length);
			}
			dtos.add(dto);
		}
		return dtos;
	}
}
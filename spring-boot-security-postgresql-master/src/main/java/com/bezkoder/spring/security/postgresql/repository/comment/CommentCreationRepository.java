package com.bezkoder.spring.security.postgresql.repository.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.entity.comment.CommentCreation;

/**
 * 
 * @author Siya
 *
 */
@Repository
public interface CommentCreationRepository extends JpaRepository<CommentCreation, Long> {

	CommentCreation findByCommentId(Long commentId);

	@Query("select h from CommentCreation h where h.isDeleted='N' And h.userId=:userId")
	List<CommentCreation> listByCommentId(Long userId);

	@Query("select h from CommentCreation h where h.isDeleted='N' And h.postId=:postId")
	List<CommentCreation> findBypostId(Long postId);

}

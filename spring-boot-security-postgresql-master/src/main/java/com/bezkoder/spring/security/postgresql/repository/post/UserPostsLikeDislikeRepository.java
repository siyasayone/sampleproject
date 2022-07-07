package com.bezkoder.spring.security.postgresql.repository.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.entity.post.UserPostLikeDislike;

/**
 * 
 * @author Siya
 *
 */
@Repository
public interface UserPostsLikeDislikeRepository extends JpaRepository<UserPostLikeDislike, Long> {

	UserPostLikeDislike findByUserLikeDislikeId(Long userLikeDislikeId);

	@Query("select h from UserPostLikeDislike h where h.isDeleted='N'")
	List<UserPostLikeDislike> listByUserLikeDislikeId();

	@Query("select h from UserPostLikeDislike h where h.isDeleted='N' And h.postId=:postId")
	List<UserPostLikeDislike> findBypostId(Long postId);

}

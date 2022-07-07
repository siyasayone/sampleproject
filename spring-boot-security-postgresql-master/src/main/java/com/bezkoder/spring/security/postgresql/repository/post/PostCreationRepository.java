package com.bezkoder.spring.security.postgresql.repository.post;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.entity.post.PostCreation;

/**
 * 
 * @author Siya
 *
 */
@Repository
public interface PostCreationRepository extends JpaRepository<PostCreation, Long> {

	@Query("select isDeleted from PostCreation h where h.isDeleted='N' and h.postId=:postId")
	String findByPostId(Long postId);

	Boolean existsByPostId(Long postId);

	@Transactional
	@Query("select h from PostCreation h where h.isDeleted='N' and h.userId=:userId")
	List<PostCreation> listByPostId(Long userId);

	@Modifying
	@Transactional
	@Query("UPDATE PostCreation p SET p.isDeleted='Y' WHERE p.postId=:postId")
	void updatePosts(Long postId);
}

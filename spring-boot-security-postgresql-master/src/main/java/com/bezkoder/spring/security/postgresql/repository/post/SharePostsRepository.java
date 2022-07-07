package com.bezkoder.spring.security.postgresql.repository.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.entity.post.SharePosts;

/**
 * 
 * @author Siya
 *
 */
@Repository
public interface SharePostsRepository extends JpaRepository<SharePosts, Long> {

	SharePosts findBySharePostsId(Long sharePostsId);

	@Query("select h from TagPosts h where h.isDeleted='N'")
	List<SharePosts> listByAll();

	@Query("select h from SharePosts h where h.isDeleted='N' And h.postId=:postId")
	List<SharePosts> findBypostId(Long postId);
}

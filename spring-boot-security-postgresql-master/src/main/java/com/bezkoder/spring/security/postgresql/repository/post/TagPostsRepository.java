package com.bezkoder.spring.security.postgresql.repository.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.entity.post.TagPosts;

/**
 * 
 * @author Siya
 *
 */
@Repository
public interface TagPostsRepository extends JpaRepository<TagPosts, Long> {

	TagPosts findByTagPostsId(Long tagPostsId);

	@Query("select h from TagPosts h where h.isDeleted='N'")
	List<TagPosts> listByTagPostsId();

	@Query("select h from TagPosts h where h.isDeleted='N' And h.postId=:postId")
	List<TagPosts> findBypostId(Long postId);

}

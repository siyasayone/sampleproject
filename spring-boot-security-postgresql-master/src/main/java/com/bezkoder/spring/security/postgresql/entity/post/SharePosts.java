package com.bezkoder.spring.security.postgresql.entity.post;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Siya
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharePosts {

	@Id
	@GeneratedValue(generator = "sharePostsId_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sharePostsId_seq", sequenceName = "sharePostsId_seq", allocationSize = 1)
	private Long sharePostsId;

	@Column(length = 10)
	private Long postId;

	@Column(length = 10)
	private Long userId;

	@Column(length = 50)
	private String modifiedBy;

	private Date createdDate;

	private Date modifiedDate;

	@Column(length = 1)
	private String isDeleted;

}
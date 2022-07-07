package com.bezkoder.spring.security.postgresql.entity.comment;

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
public class CommentCreation {

	@Id
	@GeneratedValue(generator = "commentId_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "commentId_seq", sequenceName = "commentId_seq", allocationSize = 1)
	private Long commentId;

	@Column(length = 50)
	private Long userId;

	@Column(length = 50)
	private Long postId;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@Column(length = 50)
	private String commentedBy;

	private Date createdDate;

	private Date modifiedDate;

	@Column(length = 1)
	private String isDeleted;

}
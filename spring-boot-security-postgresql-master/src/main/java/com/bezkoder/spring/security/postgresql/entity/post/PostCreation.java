package com.bezkoder.spring.security.postgresql.entity.post;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
public class PostCreation {

	@Id
	@GeneratedValue(generator = "postId_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "postId_seq", sequenceName = "postId_seq", allocationSize = 1)
	private Long postId;

	@Column(length = 50)
	private Long userId;

	private String name;

	private String type;

	private String caption;

	@Lob
	private byte[] data;

	@Column(length = 50)
	private String modifiedBy;

	private Date createdDate;

	private Date modifiedDate;

	@Column(length = 1)
	private String isDeleted;

}
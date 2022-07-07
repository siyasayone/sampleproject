package com.bezkoder.spring.security.postgresql.entity.passwordToken;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = "passwordToken")
public class PasswordToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "passwordTokenid_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "passwordTokenid_seq", sequenceName = "passwordTokenid_seq", allocationSize = 1)
	@Column(name = "passwordTokenid")
	private Long passwordTokenid;

	@Column(length = 100)
	private Long userId;

	@Column(length = 100)
	private String userName;

	@Column(length = 100, name = "onlinestatus")
	private String onlineStatus = "N";

	@Column(length = 100)
	private Date loginTime;

	@Column(length = 100)
	private Date lastUpdatedTime;

	@Column(length = 1000)
	private String token = "";

}

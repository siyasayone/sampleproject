package com.bezkoder.spring.security.postgresql.repository.user;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.entity.passwordToken.PasswordToken;

/**
 * 
 * @author Siya
 *
 */
@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {

	@Query(value = "SELECT a FROM PasswordToken a WHERE a.userId = :userId AND a.token = :token ORDER BY a.passwordTokenid DESC")
	PasswordToken findByUserId(@Param("userId") Long userId, @Param("token") String token);

	@Query(value = "SELECT a FROM PasswordToken a WHERE  a.token = :token AND a.onlineStatus = 'Y'")
	PasswordToken findBytoken(@Param("token") String token);

	@Transactional
	@Modifying
	@Query(value = "UPDATE PasswordToken a SET a.onlineStatus = 'N'  "
			+ "WHERE a.onlineStatus != 'N' AND a.userId = :id ")
	void updatePasswordTokenUserId(Long id);

}

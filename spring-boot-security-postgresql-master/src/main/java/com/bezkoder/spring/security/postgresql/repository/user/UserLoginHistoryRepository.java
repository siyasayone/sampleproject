package com.bezkoder.spring.security.postgresql.repository.user;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.entity.user.UserLoginHistory;

/**
 * 
 * @author Siya
 *
 */
@Repository
public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {

	@Query(value = "SELECT a FROM UserLoginHistory a WHERE a.userId = :userId AND a.token = :token ORDER BY a.loginHistoryId DESC")
	UserLoginHistory findByUserId(@Param("userId") Long userId, @Param("token") String token);

	@Query(value = "SELECT a FROM UserLoginHistory a WHERE  a.token = :token AND a.onlineStatus='Y'")
	UserLoginHistory findBytoken(@Param("token") String token);

	@Transactional
	@Modifying
	@Query(value = "UPDATE UserLoginHistory a SET a.onlineStatus = 'N'  "
			+ "WHERE a.onlineStatus != 'N' AND a.userId = :id ")
	void updateUserLoginUserId(Long id);

}

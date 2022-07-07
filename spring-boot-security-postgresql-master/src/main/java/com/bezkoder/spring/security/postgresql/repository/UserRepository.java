package com.bezkoder.spring.security.postgresql.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	User findByUsernameIs(String username);

	User findByEmailIs(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Query("select h from User h where h.isDeleted='N' and h.id=:id")
	User findByUserRegistrationIdAndIsDeleted(Long id);

	@Query("select h from User h where h.isDeleted='N'")
	List<User> listByUserRegistrationId();

	@Query("SELECT a FROM User a WHERE a.email= :email")
	User checkMail(@Param("email") String email);
}

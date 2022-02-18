package com.learning.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.dto.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsById(Long id);
	Boolean existsByEmail(String email);
	Boolean existsByUsername(String username);
	Boolean existsByEmailAndPassword(String email, String password);
	Optional<User> findByUsername(String username);
}

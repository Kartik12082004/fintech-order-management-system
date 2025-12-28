package com.kartik.Trading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartik.Trading.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}

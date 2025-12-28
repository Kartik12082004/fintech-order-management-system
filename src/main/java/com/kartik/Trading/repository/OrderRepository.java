package com.kartik.Trading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartik.Trading.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<Order> findByIdempotencyKey(String idempotencyKey);
}

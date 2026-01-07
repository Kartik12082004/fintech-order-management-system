package com.kartik.Trading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartik.Trading.model.Trade;
import com.kartik.Trading.model.User;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	List<Trade> findByUser(User user);
}

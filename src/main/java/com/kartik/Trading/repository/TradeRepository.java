package com.kartik.Trading.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kartik.Trading.model.Asset;
import com.kartik.Trading.model.Trade;
import com.kartik.Trading.model.User;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	List<Trade> findByUser(User user);
	List<Trade> findByUserOrderByExecutedAtDesc(User user);
	
	@Query("""
			SELECT COALESCE(SUM(
				CASE
					WHEN t.tradeType = 'BUY' THEN t.quantity
					WHEN t.tradeType = 'SELL' THEN -t.quantity
				END
			), 0)
			FROM Trade t
			WHERE t.user = :user AND t.asset = :asset
	""")
	BigDecimal getNetQuantity(@Param("user") User user,
			@Param("asset") Asset asset);
}

package com.kartik.Trading;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.kartik.Trading.model.Role;
import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.service.WalletService;

@Testcontainers
public class WalletOptimisticLockingTest extends BaseIntegrationTest {

	@Autowired
	private WalletService walletService;
	
	@Test
	void preventDoubleSpendWithOptimisticLocking() throws InterruptedException {
		
		User targetUser = new User();
		targetUser.setEmail("victim@kartik.com");
		targetUser.setPassword("password123");
		targetUser.setRole(Role.ROLE_USER);
		targetUser = userRepository.save(targetUser);
		
		Wallet wallet = new Wallet(targetUser);
		walletRepository.save(wallet);
		
		walletService.systemCredit(targetUser, new BigDecimal("100.00"));
		
		int threadCount = 2;
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(threadCount);
		
		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger lockExceptionCount = new AtomicInteger(0);
		
		final User finalUser = targetUser;
		Runnable attackTask = () -> {
			try {
				startLatch.await();
				walletService.debit(finalUser, new BigDecimal("80.00"));
				successCount.incrementAndGet();
			} catch (ObjectOptimisticLockingFailureException e) {
				lockExceptionCount.incrementAndGet();
			} catch (Exception e) {
				System.out.println("Unexpected exception: " + e.getMessage());
			} finally {
				doneLatch.countDown();
			}
		};
		
		executor.submit(attackTask);
		executor.submit(attackTask);
		
		startLatch.countDown();
		
		doneLatch.await();
		
		assertEquals(1, successCount.get(), "Fatal Flaw: Both withdrawals succeeded!");
		
		assertEquals(1, lockExceptionCount.get(), "Defense Failed: Optimistic Locking did not trigger!");
		
		Wallet survivingWallet = walletRepository.findByUserId(targetUser.getId()).orElseThrow();
		assertEquals(0, new BigDecimal("20.00").compareTo(survivingWallet.getBalance()),
				"Database Corruption: Balance is incorrect after concurrent attack!");
	}
	
}

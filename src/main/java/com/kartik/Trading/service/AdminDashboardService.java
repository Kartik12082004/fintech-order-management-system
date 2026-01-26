package com.kartik.Trading.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.AdminDashboardResponse;
import com.kartik.Trading.model.TradeType;
import com.kartik.Trading.repository.AssetRepository;
import com.kartik.Trading.repository.TradeRepository;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.repository.WalletRepository;

@Service
public class AdminDashboardService {

    private final WalletRepository walletRepository;

    private final TradeRepository tradeRepository;

    private final AssetRepository assetRepository;

    private final UserRepository userRepository;

	   public AdminDashboardService(
	            UserRepository userRepository,
	            AssetRepository assetRepository,
	            TradeRepository tradeRepository,
	            WalletRepository walletRepository
	   ) {
	        this.userRepository = userRepository;
	        this.assetRepository = assetRepository;
	        this.tradeRepository = tradeRepository;
	        this.walletRepository = walletRepository;
	    }
	   
	   @Transactional(readOnly = true)
	   public AdminDashboardResponse getSummary() {
		   
		   return new AdminDashboardResponse(
				   userRepository.count(),
				   assetRepository.count(),
				   tradeRepository.count(),
				   walletRepository.getTotalBalance(),
				   tradeRepository.countByTradeType(TradeType.BUY),
				   tradeRepository.countByTradeType(TradeType.SELL)
		  );
		   
	   }
	   
}

package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.PortfolioResponse;
import com.kartik.Trading.model.Asset;
import com.kartik.Trading.model.TradeType;
import com.kartik.Trading.model.User;
import com.kartik.Trading.repository.TradeRepository;

@Service
public class PortfolioService {
	
	private final TradeRepository tradeRepository;
	
	public PortfolioService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }
	
	@Transactional(readOnly = true)
	public  List<PortfolioResponse> getPortfolio(User user){
		
		return tradeRepository.findByUser(user)
				.stream()
				.collect(Collectors.groupingBy(trade -> trade.getAsset()))
				.entrySet()
				.stream()
				.map(entry -> {
					
					Asset asset = entry.getKey();
					
					BigDecimal netQuantity = entry.getValue()
							.stream()
							.map(trade -> 
									trade.getTradeType() == TradeType.BUY
									? trade.getQuantity()
									: trade.getQuantity().negate()
							)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					
					if(netQuantity.compareTo(BigDecimal.ZERO) <= 0) {
						return null;
					}
					
					BigDecimal price = asset.getPrice();
                    BigDecimal value = netQuantity.multiply(price);
                    
                    return new PortfolioResponse(
                            asset.getSymbol(),
                            netQuantity,
                            price,
                            value
                    );
					
				})
				.filter(Objects::nonNull)
				.toList();
		
	}
	
}

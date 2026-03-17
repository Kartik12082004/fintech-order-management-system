package com.kartik.MiniOrderManagementSystem;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.kartik.Trading.TradingApplication;
import com.kartik.Trading.repository.AdminAuditLogRepository;
import com.kartik.Trading.repository.AssetRepository;
import com.kartik.Trading.repository.TradeRepository;
import com.kartik.Trading.repository.TransactionRepository;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.repository.WalletRepository;

@SpringBootTest(classes = TradingApplication.class)
@Testcontainers
public abstract class BaseIntegrationTest {

	static {
		java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("UTC"));
	}
	
	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("test_db")
			.withUsername("test_user")
			.withPassword("test_pass");
	
	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		
		registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
		registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
		
		registry.add("jwt.secret", () -> "testSecretKeyThatIsAtLeast32BytesLongForJwtValidation1234567890");
	}
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
    protected WalletRepository walletRepository;

    @Autowired
    protected AssetRepository assetRepository;

    @Autowired
    protected TradeRepository tradeRepository;

    @Autowired
    protected TransactionRepository transactionRepository;
    
    @Autowired
    protected AdminAuditLogRepository adminAuditLogRepository;
    
    @BeforeEach
    void cleanDatabase() {
    	adminAuditLogRepository.deleteAllInBatch();
        transactionRepository.deleteAllInBatch();
        tradeRepository.deleteAllInBatch();
        walletRepository.deleteAllInBatch();
        assetRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
	
}

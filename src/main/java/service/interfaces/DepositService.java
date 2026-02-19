package service.interfaces;

import java.math.BigDecimal;

public interface DepositService {
	
	void deposit(Long userId, String currencyCode, BigDecimal amount, String method);
}
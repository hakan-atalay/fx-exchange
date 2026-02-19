package service.interfaces;

import entity.ExchangeRate;
import java.math.BigDecimal;

public interface ExchangeRateService {
	
	ExchangeRate getOrFetchRate(String baseCurrency, String targetCurrency, BigDecimal rate, String source);
}
package service.interfaces;

import dto.response.WithdrawalResponseDTO;

import java.math.BigDecimal;

public interface WithdrawalService {
	
	WithdrawalResponseDTO withdraw(Long userId, String currencyCode, BigDecimal amount, String iban);
}
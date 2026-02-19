package service.interfaces;

import dto.request.ExchangeCreateDTO;
import dto.response.TransactionResponseDTO;

import java.math.BigDecimal;

public interface ExchangeService {
	
	TransactionResponseDTO exchange(Long userId, ExchangeCreateDTO request, BigDecimal rate);
}
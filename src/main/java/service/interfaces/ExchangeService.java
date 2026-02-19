package service.interfaces;

import dto.request.ExchangeRequestDTO;
import dto.response.TransactionResponseDTO;

import java.math.BigDecimal;

public interface ExchangeService {
	
	TransactionResponseDTO exchange(Long userId, ExchangeRequestDTO request, BigDecimal rate);
}
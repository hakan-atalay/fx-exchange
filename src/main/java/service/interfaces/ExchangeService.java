package service.interfaces;

import dto.request.ExchangeCreateDTO;
import dto.response.TransactionResponseDTO;

public interface ExchangeService {

	TransactionResponseDTO exchange(Long userId, ExchangeCreateDTO request);
}
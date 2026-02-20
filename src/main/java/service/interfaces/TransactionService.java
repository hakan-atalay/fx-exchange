package service.interfaces;

import dto.response.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {
	
	List<TransactionResponseDTO> getAllTransactions();
	
	TransactionResponseDTO getTransactionById(Long id);
	
}
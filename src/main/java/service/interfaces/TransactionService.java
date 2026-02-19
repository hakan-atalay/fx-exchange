package service.interfaces;

import dto.response.TransactionResponseDTO;
import entity.Transaction;

import java.util.List;

public interface TransactionService {
	
	List<TransactionResponseDTO> getAllTransactions();
	
	TransactionResponseDTO getTransactionById(Long id);
	
	TransactionResponseDTO createTransaction(Transaction transaction);
}
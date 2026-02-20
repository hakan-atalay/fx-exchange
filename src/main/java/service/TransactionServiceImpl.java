package service;

import dao.TransactionDAO;
import dto.response.TransactionResponseDTO;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mapper.TransactionMapper;
import service.interfaces.TransactionService;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransactionServiceImpl implements TransactionService{

	@Inject
	private TransactionDAO transactionDAO;

	public List<TransactionResponseDTO> getAllTransactions() {
		return transactionDAO.findAll().stream().map(TransactionMapper::toResponse).collect(Collectors.toList());
	}

	public TransactionResponseDTO getTransactionById(Long id) {
		return transactionDAO.findById(id).map(TransactionMapper::toResponse)
				.orElseThrow(() -> new ServiceException("Transaction not found"));
	}

}
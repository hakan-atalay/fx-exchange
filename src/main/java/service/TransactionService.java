package service;

import dao.TransactionDAO;
import dto.response.TransactionResponseDTO;
import entity.Transaction;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mapper.TransactionMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransactionService {

	@Inject
	private TransactionDAO transactionDAO;

	@Inject
	private DataSource dataSource;

	public List<TransactionResponseDTO> getAllTransactions() {
		return transactionDAO.findAll().stream().map(TransactionMapper::toResponse).collect(Collectors.toList());
	}

	public TransactionResponseDTO getTransactionById(Long id) {
		return transactionDAO.findById(id).map(TransactionMapper::toResponse)
				.orElseThrow(() -> new ServiceException("Transaction not found"));
	}

	public TransactionResponseDTO createTransaction(Transaction transaction) {
		try (Connection con = dataSource.getConnection()) {
			con.setAutoCommit(false);
			Transaction saved = transactionDAO.save(con, transaction);
			con.commit();
			return TransactionMapper.toResponse(saved);
		} catch (SQLException e) {
			throw new ServiceException("Failed to create transaction", e);
		}
	}

}
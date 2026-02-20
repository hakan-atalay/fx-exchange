package service;

import dao.WalletDAO;
import dao.WithdrawalDAO;
import dto.request.WithdrawalCreateDTO;
import dto.response.WithdrawalResponseDTO;
import entity.Withdrawal;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import service.interfaces.WithdrawalService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@ApplicationScoped
public class WithdrawalServiceImpl implements WithdrawalService{

	@Inject
	private WalletDAO walletDAO;

	@Inject
	private WithdrawalDAO withdrawalDAO;

	@Inject
	private DataSource dataSource;

	public WithdrawalResponseDTO withdraw(Long userId, WithdrawalCreateDTO request) {
		validateRequest(userId, request);

		try (Connection con = dataSource.getConnection()) {
			con.setAutoCommit(false);

			boolean debited = walletDAO.atomicDebit(con, userId, request.getCurrencyCode().toUpperCase(),
					request.getAmount());
			if (!debited)
				throw new ServiceException("Insufficient balance");

			Withdrawal withdrawal = new Withdrawal();
			withdrawal.setUserId(userId);
			withdrawal.setCurrencyCode(request.getCurrencyCode().toUpperCase());
			withdrawal.setAmount(request.getAmount());
			withdrawal.setIban(request.getIban());
			withdrawal.setStatus("COMPLETED");
			withdrawal.setCreatedAt(LocalDateTime.now());

			withdrawalDAO.save(con, withdrawal);

			con.commit();

			return new WithdrawalResponseDTO(withdrawal.getId(), withdrawal.getCurrencyCode(), withdrawal.getAmount(),
					withdrawal.getIban(), withdrawal.getStatus(), withdrawal.getCreatedAt());

		} catch (SQLException e) {
			throw new ServiceException("Database error during withdrawal", e);
		} catch (Exception e) {
			throw new ServiceException("Withdrawal transaction failed", e);
		}
	}

	private void validateRequest(Long userId, WithdrawalCreateDTO request) {
		if (userId == null || userId <= 0)
			throw new ServiceException("Invalid user ID");
		if (request == null)
			throw new ServiceException("Withdrawal request is required");
		if (request.getCurrencyCode() == null || request.getCurrencyCode().isBlank())
			throw new ServiceException("Invalid currency code");
		if (request.getAmount() == null || request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0)
			throw new ServiceException("Amount must be greater than zero");
	}
}

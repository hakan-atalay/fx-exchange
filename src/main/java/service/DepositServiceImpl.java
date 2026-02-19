package service;

import dao.DepositDAO;
import dao.WalletDAO;
import dto.request.DepositCreateDTO;
import dto.response.DepositResponseDTO;
import entity.Deposit;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import mapper.DepositMapper;

@ApplicationScoped
public class DepositServiceImpl {

	@Inject
	private WalletDAO walletDAO;

	@Inject
	private DepositDAO depositDAO;

	@Inject
	private DataSource dataSource;

	public DepositResponseDTO deposit(Long userId, DepositCreateDTO depositDTO) {
		validateDeposit(userId, depositDTO);

		try (Connection con = dataSource.getConnection()) {
			con.setAutoCommit(false);

			try {
				walletDAO.atomicCredit(con, userId, depositDTO.getCurrencyCode().toUpperCase(), depositDTO.getAmount());

				Deposit deposit = new Deposit();
				deposit.setUserId(userId);
				deposit.setCurrencyCode(depositDTO.getCurrencyCode().toUpperCase());
				deposit.setAmount(depositDTO.getAmount());
				deposit.setMethod(depositDTO.getMethod());
				deposit.setStatus("COMPLETED");
				deposit.setCreatedAt(LocalDateTime.now());

				depositDAO.save(con, deposit);

				con.commit();

				return DepositMapper.toResponse(deposit);
			} catch (Exception e) {
				con.rollback();
				throw new ServiceException("Deposit transaction failed", e);
			}
		} catch (SQLException e) {
			throw new ServiceException("Failed to connect to the database", e);
		}
	}

	private void validateDeposit(Long userId, DepositCreateDTO depositDTO) {
		if (userId == null || userId <= 0)
			throw new ServiceException("Invalid userId");
		if (depositDTO.getCurrencyCode() == null || depositDTO.getCurrencyCode().isBlank())
			throw new ServiceException("Invalid currency");
		if (depositDTO.getAmount() == null || depositDTO.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0)
			throw new ServiceException("Invalid amount");
	}
}

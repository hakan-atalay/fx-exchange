package service;

import dao.TransactionDAO;
import dao.WalletDAO;
import dto.request.ExchangeCreateDTO;
import dto.response.TransactionResponseDTO;
import entity.Transaction;
import entity.Wallet;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mapper.TransactionMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@ApplicationScoped
public class ExchangeServiceImpl {

	@Inject
	private WalletDAO walletDAO;

	@Inject
	private TransactionDAO transactionDAO;

	@Inject
	private DataSource dataSource;

	public TransactionResponseDTO exchange(Long userId, ExchangeCreateDTO request, BigDecimal rate) {
		validateRequest(userId, request, rate);

		String fromCurrency = request.getFromCurrencyCode().toUpperCase();
		String toCurrency = request.getToCurrencyCode().toUpperCase();
		BigDecimal amountFrom = request.getAmount();
		BigDecimal amountTo = calculateAmountTo(amountFrom, rate);

		try (Connection con = dataSource.getConnection()) {
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			Wallet fromWallet = walletDAO.lockWallet(con, userId, fromCurrency);
			Wallet toWallet = walletDAO.lockWallet(con, userId, toCurrency);

			checkBalance(fromWallet, amountFrom);

			performWalletExchange(con, userId, fromCurrency, toCurrency, amountFrom, amountTo);

			Transaction tx = createTransaction(userId, fromCurrency, toCurrency, amountFrom, amountTo, rate);
			transactionDAO.save(con, tx);

			con.commit();
			return TransactionMapper.toResponse(tx);

		} catch (Exception e) {
			throw e instanceof ServiceException ? (ServiceException) e : new ServiceException("Exchange failed", e);
		}
	}

	private void validateRequest(Long userId, ExchangeCreateDTO request, BigDecimal rate) {
		if (userId == null || userId <= 0)
			throw new ServiceException("User id is required");
		if (request == null)
			throw new ServiceException("Exchange request is required");
		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0)
			throw new ServiceException("Amount must be positive");
		if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0)
			throw new ServiceException("Rate must be positive");

		String from = request.getFromCurrencyCode();
		String to = request.getToCurrencyCode();
		if (from == null || to == null || from.equalsIgnoreCase(to))
			throw new ServiceException("Invalid currency pair");
	}

	private BigDecimal calculateAmountTo(BigDecimal amountFrom, BigDecimal rate) {
		return amountFrom.multiply(rate).setScale(4, RoundingMode.HALF_UP);
	}

	private void checkBalance(Wallet fromWallet, BigDecimal amountFrom) {
		if (fromWallet.getBalance().compareTo(amountFrom) < 0)
			throw new ServiceException("Insufficient balance");
	}

	private void performWalletExchange(Connection con, Long userId, String fromCurrency, String toCurrency,
			BigDecimal amountFrom, BigDecimal amountTo) throws SQLException {
		walletDAO.atomicDebit(con, userId, fromCurrency, amountFrom);
		walletDAO.atomicCredit(con, userId, toCurrency, amountTo);
	}

	private Transaction createTransaction(Long userId, String fromCurrency, String toCurrency, BigDecimal amountFrom,
			BigDecimal amountTo, BigDecimal rate) {
		Transaction tx = new Transaction();
		tx.setUserId(userId);
		tx.setFromCurrencyCode(fromCurrency);
		tx.setToCurrencyCode(toCurrency);
		tx.setAmountFrom(amountFrom);
		tx.setAmountTo(amountTo);
		tx.setExchangeRate(rate);
		tx.setTransactionType("EXCHANGE");
		tx.setStatus("COMPLETED");
		tx.setCreatedAt(LocalDateTime.now());
		return tx;
	}
}

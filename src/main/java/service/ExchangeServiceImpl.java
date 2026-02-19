package service;

import dao.TransactionDAO;
import dao.WalletDAO;
import dto.request.ExchangeRequestDTO;
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

    public TransactionResponseDTO exchange(Long userId,
                                           ExchangeRequestDTO request,
                                           BigDecimal rate) {

        if (userId == null || userId <= 0)
            throw new ServiceException("User id is required");

        if (request == null)
            throw new ServiceException("Exchange request is required");

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException("Amount must be positive");

        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException("Rate must be positive");

        String fromCurrency = request.getFromCurrencyCode();
        String toCurrency = request.getToCurrencyCode();

        if (fromCurrency == null || toCurrency == null ||
                fromCurrency.equalsIgnoreCase(toCurrency))
            throw new ServiceException("Invalid currency pair");

        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        BigDecimal amountFrom = request.getAmount();
        BigDecimal amountTo = amountFrom
                .multiply(rate)
                .setScale(4, RoundingMode.HALF_UP);

        try (Connection con = dataSource.getConnection()) {

            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {

                Wallet fromWallet = walletDAO.lockWallet(con, userId, fromCurrency);
                Wallet toWallet = walletDAO.lockWallet(con, userId, toCurrency);

                if (fromWallet.getBalance().compareTo(amountFrom) < 0)
                    throw new ServiceException("Insufficient balance");

                walletDAO.atomicDebit(con, userId, fromCurrency, amountFrom);
                walletDAO.atomicCredit(con, userId, toCurrency, amountTo);

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

                transactionDAO.save(con, tx);

                con.commit();

                return TransactionMapper.toResponse(tx);

            } catch (Exception e) {
                con.rollback();
                throw e instanceof ServiceException
                        ? (ServiceException) e
                        : new ServiceException("Exchange failed", e);
            }

        } catch (SQLException e) {
            throw new ServiceException("Exchange failed", e);
        }
    }
}

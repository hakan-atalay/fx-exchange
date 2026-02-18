package service;

import dao.WalletDAO;
import dto.response.WalletResponseDTO;
import entity.Wallet;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mapper.WalletMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WalletService {

    @Inject
    private WalletDAO walletDAO;

    @Inject
    private DataSource dataSource;

    public WalletResponseDTO createWallet(Long userId, String currencyCode) {
        validateUserId(userId);
        validateCurrency(currencyCode);

        walletDAO.findByUserIdAndCurrency(userId, currencyCode.toUpperCase())
                .ifPresent(w -> {
                    throw new ServiceException("Wallet already exists for currency: " + currencyCode);
                });

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setCurrencyCode(currencyCode.toUpperCase());
        wallet.setBalance(BigDecimal.ZERO);

        Wallet saved = walletDAO.save(wallet);
        return WalletMapper.toResponse(saved);
    }

    public List<WalletResponseDTO> getUserWallets(Long userId) {
        validateUserId(userId);

        return walletDAO.findByUserId(userId)
                .stream()
                .map(WalletMapper::toResponse)
                .collect(Collectors.toList());
    }

    public WalletResponseDTO getWallet(Long userId, String currencyCode) {
        validateUserId(userId);
        validateCurrency(currencyCode);

        Wallet wallet = walletDAO.findByUserIdAndCurrency(userId, currencyCode.toUpperCase())
                .orElseThrow(() ->
                        new ServiceException("Wallet not found for currency: " + currencyCode));

        return WalletMapper.toResponse(wallet);
    }

    public void credit(Long userId, String currencyCode, BigDecimal amount) {
        validateUserId(userId);
        validateCurrency(currencyCode);
        validateAmount(amount);

        try (Connection con = dataSource.getConnection()) {

            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {

                walletDAO.atomicCredit(con, userId, currencyCode.toUpperCase(), amount);

                con.commit();

            } catch (Exception e) {
                con.rollback();
                throw e instanceof ServiceException
                        ? (ServiceException) e
                        : new ServiceException("Credit failed", e);
            }

        } catch (SQLException e) {
            throw new ServiceException("Credit failed", e);
        }
    }

    public void debit(Long userId, String currencyCode, BigDecimal amount) {
        validateUserId(userId);
        validateCurrency(currencyCode);
        validateAmount(amount);

        try (Connection con = dataSource.getConnection()) {

            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {

                walletDAO.atomicDebit(con, userId, currencyCode.toUpperCase(), amount);

                con.commit();

            } catch (Exception e) {
                con.rollback();
                throw e instanceof ServiceException
                        ? (ServiceException) e
                        : new ServiceException("Debit failed", e);
            }

        } catch (SQLException e) {
            throw new ServiceException("Debit failed", e);
        }
    }

    public WalletResponseDTO ensureWalletExists(Long userId, String currencyCode) {
        validateUserId(userId);
        validateCurrency(currencyCode);

        Wallet wallet = walletDAO.findByUserIdAndCurrency(userId, currencyCode.toUpperCase())
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setUserId(userId);
                    w.setCurrencyCode(currencyCode.toUpperCase());
                    w.setBalance(BigDecimal.ZERO);
                    return walletDAO.save(w);
                });

        return WalletMapper.toResponse(wallet);
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0)
            throw new ServiceException("Invalid userId");
    }

    private void validateCurrency(String currencyCode) {
        if (currencyCode == null || currencyCode.isBlank())
            throw new ServiceException("Currency code cannot be empty");
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException("Amount must be greater than zero");
    }
}

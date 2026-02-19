package service;

import dao.DepositDAO;
import dao.WalletDAO;
import entity.Deposit;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@ApplicationScoped
public class DepositServiceImpl {

    @Inject
    private WalletDAO walletDAO;

    @Inject
    private DepositDAO depositDAO;

    @Inject
    private DataSource dataSource;

    public void deposit(Long userId, String currencyCode, BigDecimal amount, String method) {
        validateDeposit(userId, currencyCode, amount);

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            try {
                walletDAO.atomicCredit(con, userId, currencyCode.toUpperCase(), amount);

                Deposit deposit = new Deposit();
                deposit.setUserId(userId);
                deposit.setCurrencyCode(currencyCode.toUpperCase());
                deposit.setAmount(amount);
                deposit.setMethod(method);
                deposit.setStatus("COMPLETED");
                deposit.setCreatedAt(LocalDateTime.now());

                depositDAO.save(con, deposit);

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new ServiceException("Deposit transaction failed", e);
            }
        } catch (SQLException e) {
            throw new ServiceException("Failed to connect to the database", e);
        }
    }

    private void validateDeposit(Long userId, String currencyCode, BigDecimal amount) {
        if (userId == null || userId <= 0) throw new ServiceException("Invalid userId");
        if (currencyCode == null || currencyCode.isBlank()) throw new ServiceException("Invalid currency");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new ServiceException("Invalid amount");
    }
}

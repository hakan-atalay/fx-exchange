package service;

import dao.WalletDAO;
import dao.WithdrawalDAO;
import dto.response.WithdrawalResponseDTO;
import entity.Withdrawal;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@ApplicationScoped
public class WithdrawalService {

    @Inject
    private WalletDAO walletDAO;

    @Inject
    private WithdrawalDAO withdrawalDAO;

    @Inject
    private DataSource dataSource;

    public WithdrawalResponseDTO withdraw(Long userId,
                                          String currencyCode,
                                          BigDecimal amount,
                                          String iban) {

        validateInput(userId, currencyCode, amount);

        try (Connection con = dataSource.getConnection()) {

            con.setAutoCommit(false);

            try {

                boolean success = walletDAO.atomicDebit(con, userId, currencyCode.toUpperCase(), amount);
                if (!success) throw new ServiceException("Insufficient balance");

                Withdrawal withdrawal = buildWithdrawal(userId, currencyCode, amount, iban);
                withdrawalDAO.save(con, withdrawal);

                con.commit();

                return mapToDTO(withdrawal);

            } catch (Exception e) {
                con.rollback();
                throw new ServiceException("Withdrawal transaction failed", e);
            }

        } catch (SQLException e) {
            throw new ServiceException("Database connection error during withdrawal", e);
        }
    }

    private void validateInput(Long userId, String currencyCode, BigDecimal amount) {
        if (userId == null || userId <= 0) throw new ServiceException("Invalid user ID");
        if (currencyCode == null || currencyCode.isBlank()) throw new ServiceException("Invalid currency code");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException("Amount must be greater than zero");
    }

    private Withdrawal buildWithdrawal(Long userId, String currencyCode, BigDecimal amount, String iban) {
        Withdrawal w = new Withdrawal();
        w.setUserId(userId);
        w.setCurrencyCode(currencyCode.toUpperCase());
        w.setAmount(amount);
        w.setIban(iban);
        w.setStatus("COMPLETED");
        w.setCreatedAt(LocalDateTime.now());
        return w;
    }

    private WithdrawalResponseDTO mapToDTO(Withdrawal w) {
        return new WithdrawalResponseDTO(
                w.getId(),
                w.getCurrencyCode(),
                w.getAmount(),
                w.getIban(),
                w.getStatus(),
                w.getCreatedAt()
        );
    }
}

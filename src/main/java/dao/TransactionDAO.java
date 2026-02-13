package dao;

import entity.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TransactionDAO extends BaseDAO implements GenericDAO<Transaction, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO transactions " +
            "(user_id, from_currency_code, to_currency_code, amount_from, amount_to, exchange_rate, transaction_type, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String UPDATE_SQL =
            "UPDATE transactions SET " +
            "user_id=?, from_currency_code=?, to_currency_code=?, amount_from=?, amount_to=?, exchange_rate=?, transaction_type=?, status=? " +
            "WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM transactions WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM transactions WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM transactions ORDER BY id DESC";

    @Override
    public Transaction save(Transaction transaction) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setLong(1, transaction.getUserId());
            ps.setString(2, transaction.getFromCurrencyCode());
            ps.setString(3, transaction.getToCurrencyCode());
            ps.setBigDecimal(4, transaction.getAmountFrom());
            ps.setBigDecimal(5, transaction.getAmountTo());
            ps.setBigDecimal(6, transaction.getExchangeRate());
            ps.setString(7, transaction.getTransactionType());
            ps.setString(8, transaction.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                transaction.setId(rs.getLong("id"));
            }

            return transaction;
        }
    }

    @Override
    public Transaction update(Transaction transaction) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setLong(1, transaction.getUserId());
            ps.setString(2, transaction.getFromCurrencyCode());
            ps.setString(3, transaction.getToCurrencyCode());
            ps.setBigDecimal(4, transaction.getAmountFrom());
            ps.setBigDecimal(5, transaction.getAmountTo());
            ps.setBigDecimal(6, transaction.getExchangeRate());
            ps.setString(7, transaction.getTransactionType());
            ps.setString(8, transaction.getStatus());
            ps.setLong(9, transaction.getId());

            ps.executeUpdate();
            return transaction;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Transaction> findById(Long id) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapRow(rs));
            }
        }

        return transactions;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setId(rs.getLong("id"));
        transaction.setUserId(rs.getLong("user_id"));
        transaction.setFromCurrencyCode(rs.getString("from_currency_code"));
        transaction.setToCurrencyCode(rs.getString("to_currency_code"));
        transaction.setAmountFrom(rs.getBigDecimal("amount_from"));
        transaction.setAmountTo(rs.getBigDecimal("amount_to"));
        transaction.setExchangeRate(rs.getBigDecimal("exchange_rate"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setStatus(rs.getString("status"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            transaction.setCreatedAt(ts.toLocalDateTime());
        }

        return transaction;
    }
}

package dao;

import entity.Withdrawal;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WithdrawalDAO extends BaseDAO implements GenericDAO<Withdrawal, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO withdrawals (user_id, currency_code, amount, iban, status) " +
            "VALUES (?, ?, ?, ?, ?) RETURNING id";

    private static final String UPDATE_SQL =
            "UPDATE withdrawals SET user_id=?, currency_code=?, amount=?, iban=?, status=? " +
            "WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM withdrawals WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM withdrawals WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM withdrawals ORDER BY id DESC";

    @Override
    public Withdrawal save(Withdrawal withdrawal) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setLong(1, withdrawal.getUserId());
            ps.setString(2, withdrawal.getCurrencyCode());
            ps.setBigDecimal(3, withdrawal.getAmount());
            ps.setString(4, withdrawal.getIban());
            ps.setString(5, withdrawal.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                withdrawal.setId(rs.getLong("id"));
            }

            return withdrawal;
        }
    }

    @Override
    public Withdrawal update(Withdrawal withdrawal) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setLong(1, withdrawal.getUserId());
            ps.setString(2, withdrawal.getCurrencyCode());
            ps.setBigDecimal(3, withdrawal.getAmount());
            ps.setString(4, withdrawal.getIban());
            ps.setString(5, withdrawal.getStatus());
            ps.setLong(6, withdrawal.getId());

            ps.executeUpdate();
            return withdrawal;
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
    public Optional<Withdrawal> findById(Long id) throws SQLException {
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
    public List<Withdrawal> findAll() throws SQLException {
        List<Withdrawal> withdrawals = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                withdrawals.add(mapRow(rs));
            }
        }

        return withdrawals;
    }

    private Withdrawal mapRow(ResultSet rs) throws SQLException {
        Withdrawal withdrawal = new Withdrawal();

        withdrawal.setId(rs.getLong("id"));
        withdrawal.setUserId(rs.getLong("user_id"));
        withdrawal.setCurrencyCode(rs.getString("currency_code"));
        withdrawal.setAmount(rs.getBigDecimal("amount"));
        withdrawal.setIban(rs.getString("iban"));
        withdrawal.setStatus(rs.getString("status"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            withdrawal.setCreatedAt(ts.toLocalDateTime());
        }

        return withdrawal;
    }
}

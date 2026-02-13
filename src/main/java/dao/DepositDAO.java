package dao;

import entity.Deposit;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepositDAO extends BaseDAO implements GenericDAO<Deposit, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO deposits (user_id, currency_code, amount, method, status) " +
            "VALUES (?, ?, ?, ?, ?) RETURNING id";

    private static final String UPDATE_SQL =
            "UPDATE deposits SET user_id=?, currency_code=?, amount=?, method=?, status=? " +
            "WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM deposits WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM deposits WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM deposits ORDER BY id DESC";

    @Override
    public Deposit save(Deposit deposit) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setLong(1, deposit.getUserId());
            ps.setString(2, deposit.getCurrencyCode());
            ps.setBigDecimal(3, deposit.getAmount());
            ps.setString(4, deposit.getMethod());
            ps.setString(5, deposit.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                deposit.setId(rs.getLong("id"));
            }

            return deposit;
        }
    }

    @Override
    public Deposit update(Deposit deposit) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setLong(1, deposit.getUserId());
            ps.setString(2, deposit.getCurrencyCode());
            ps.setBigDecimal(3, deposit.getAmount());
            ps.setString(4, deposit.getMethod());
            ps.setString(5, deposit.getStatus());
            ps.setLong(6, deposit.getId());

            ps.executeUpdate();
            return deposit;
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
    public Optional<Deposit> findById(Long id) throws SQLException {
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
    public List<Deposit> findAll() throws SQLException {
        List<Deposit> deposits = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                deposits.add(mapRow(rs));
            }
        }

        return deposits;
    }

    private Deposit mapRow(ResultSet rs) throws SQLException {
        Deposit deposit = new Deposit();

        deposit.setId(rs.getLong("id"));
        deposit.setUserId(rs.getLong("user_id"));
        deposit.setCurrencyCode(rs.getString("currency_code"));
        deposit.setAmount(rs.getBigDecimal("amount"));
        deposit.setMethod(rs.getString("method"));
        deposit.setStatus(rs.getString("status"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            deposit.setCreatedAt(ts.toLocalDateTime());
        }

        return deposit;
    }
}

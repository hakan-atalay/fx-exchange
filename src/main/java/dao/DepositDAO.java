package dao;

import entity.Deposit;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    public Deposit save(Deposit deposit) {
        Long id = executeQuery(
                INSERT_SQL,
                ps -> {
                    ps.setLong(1, deposit.getUserId());
                    ps.setString(2, deposit.getCurrencyCode());
                    ps.setBigDecimal(3, deposit.getAmount());
                    ps.setString(4, deposit.getMethod());
                    ps.setString(5, deposit.getStatus());
                },
                rs -> {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                    return null;
                }
        );

        deposit.setId(id);
        return deposit;
    }
    
    public Deposit save(Connection con, Deposit deposit) {
        Long id = executeQuery(con,
                INSERT_SQL,
                ps -> {
                    ps.setLong(1, deposit.getUserId());
                    ps.setString(2, deposit.getCurrencyCode());
                    ps.setBigDecimal(3, deposit.getAmount());
                    ps.setString(4, deposit.getMethod());
                    ps.setString(5, deposit.getStatus());
                },
                rs -> {
                    if (rs.next()) return rs.getLong("id");
                    return null;
                }
        );

        deposit.setId(id);
        return deposit;
    }


    @Override
    public Deposit update(Deposit deposit) {
        executeUpdate(
                UPDATE_SQL,
                ps -> {
                    ps.setLong(1, deposit.getUserId());
                    ps.setString(2, deposit.getCurrencyCode());
                    ps.setBigDecimal(3, deposit.getAmount());
                    ps.setString(4, deposit.getMethod());
                    ps.setString(5, deposit.getStatus());
                    ps.setLong(6, deposit.getId());
                }
        );
        return deposit;
    }

    @Override
    public void delete(Long id) {
        executeUpdate(
                DELETE_SQL,
                ps -> ps.setLong(1, id)
        );
    }

    @Override
    public Optional<Deposit> findById(Long id) {
        return executeQuery(
                FIND_BY_ID_SQL,
                ps -> ps.setLong(1, id),
                rs -> {
                    if (rs.next()) {
                        return Optional.of(mapRow(rs));
                    }
                    return Optional.empty();
                }
        );
    }

    @Override
    public List<Deposit> findAll() {
        return executeQuery(
                FIND_ALL_SQL,
                ps -> {},
                rs -> {
                    List<Deposit> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                    return list;
                }
        );
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

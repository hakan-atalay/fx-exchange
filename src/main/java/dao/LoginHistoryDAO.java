package dao;

import entity.LoginHistory;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoginHistoryDAO extends BaseDAO implements GenericDAO<LoginHistory, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO login_history (user_id, ip_address) VALUES (?, ?) RETURNING id";

    private static final String UPDATE_SQL =
            "UPDATE login_history SET user_id=?, ip_address=? WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM login_history WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM login_history WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM login_history ORDER BY login_time DESC";

    @Override
    public LoginHistory save(LoginHistory entity) {
        Long id = executeQuery(
                INSERT_SQL,
                ps -> {
                    ps.setLong(1, entity.getUserId());
                    ps.setString(2, entity.getIpAddress());
                },
                rs -> {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                    return null;
                }
        );

        entity.setId(id);
        return entity;
    }

    @Override
    public LoginHistory update(LoginHistory entity) {
        executeUpdate(
                UPDATE_SQL,
                ps -> {
                    ps.setLong(1, entity.getUserId());
                    ps.setString(2, entity.getIpAddress());
                    ps.setLong(3, entity.getId());
                }
        );
        return entity;
    }

    @Override
    public void delete(Long id) {
        executeUpdate(
                DELETE_SQL,
                ps -> ps.setLong(1, id)
        );
    }

    @Override
    public Optional<LoginHistory> findById(Long id) {
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
    public List<LoginHistory> findAll() {
        return executeQuery(
                FIND_ALL_SQL,
                ps -> {},
                rs -> {
                    List<LoginHistory> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                    return list;
                }
        );
    }

    private LoginHistory mapRow(ResultSet rs) throws SQLException {
        LoginHistory entity = new LoginHistory();
        entity.setId(rs.getLong("id"));
        entity.setUserId(rs.getLong("user_id"));
        entity.setIpAddress(rs.getString("ip_address"));

        Timestamp ts = rs.getTimestamp("login_time");
        if (ts != null) {
            entity.setLoginTime(ts.toLocalDateTime());
        }

        return entity;
    }
}

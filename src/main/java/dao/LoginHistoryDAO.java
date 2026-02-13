package dao;

import entity.LoginHistory;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoginHistoryDAO extends BaseDAO implements GenericDAO<LoginHistory, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO login_history (user_id, ip_address) " +
            "VALUES (?, ?) RETURNING id";

    private static final String UPDATE_SQL =
            "UPDATE login_history SET user_id=?, ip_address=? WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM login_history WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM login_history WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM login_history ORDER BY login_time DESC";

    @Override
    public LoginHistory save(LoginHistory entity) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setLong(1, entity.getUserId());
            ps.setString(2, entity.getIpAddress());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                entity.setId(rs.getLong("id"));
            }

            return entity;
        }
    }

    @Override
    public LoginHistory update(LoginHistory entity) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setLong(1, entity.getUserId());
            ps.setString(2, entity.getIpAddress());
            ps.setLong(3, entity.getId());

            ps.executeUpdate();
            return entity;
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
    public Optional<LoginHistory> findById(Long id) throws SQLException {
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
    public List<LoginHistory> findAll() throws SQLException {
        List<LoginHistory> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

        return list;
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

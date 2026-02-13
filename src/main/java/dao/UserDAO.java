package dao;

import entity.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDAO extends BaseDAO implements GenericDAO<User, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO users (username,email,password_hash,first_name,last_name,role,status) VALUES (?,?,?,?,?,?,?) RETURNING id";

    private static final String UPDATE_SQL =
            "UPDATE users SET username=?, email=?, password_hash=?, first_name=?, last_name=?, role=?, status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM users WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM users WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM users ORDER BY id";

    @Override
    public User save(User user) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getLong("id"));
            }
            return user;
        }
    }

    @Override
    public User update(User user) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());
            ps.setLong(8, user.getId());

            ps.executeUpdate();
            return user;
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<User> findById(Long id) throws Exception {
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
    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUserName(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    }
}

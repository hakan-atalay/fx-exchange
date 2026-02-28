package dao;

import entity.User;
import exception.DAOException;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

	private static final String FIND_BY_EMAIL_SQL = 
			"SELECT * FROM users WHERE email=?";

	private static final String FIND_ALL_SQL = 
			"SELECT * FROM users ORDER BY id";

	@Override
	public User save(User user) {
		return executeQuery(INSERT_SQL, ps -> {
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPasswordHash());
			ps.setString(4, user.getFirstName());
			ps.setString(5, user.getLastName());
			ps.setString(6, user.getRole());
			ps.setString(7, user.getStatus());
		}, rs -> {
			if (rs.next()) {
				user.setId(rs.getLong("id"));
			}
			return user;
		});
	}

	@Override
	public User update(User user) {
		int affected = executeUpdate(UPDATE_SQL, ps -> {
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPasswordHash());
			ps.setString(4, user.getFirstName());
			ps.setString(5, user.getLastName());
			ps.setString(6, user.getRole());
			ps.setString(7, user.getStatus());
			ps.setLong(8, user.getId());
		});

		if (affected == 0) {
			throw new DAOException("User update failed", null);
		}

		return user;
	}

	@Override
	public void delete(Long id) {
		int affected = executeUpdate(DELETE_SQL, ps -> ps.setLong(1, id));

		if (affected == 0) {
			throw new DAOException("User delete failed", null);
		}
	}

	@Override
	public Optional<User> findById(Long id) {
		return executeQuery(FIND_BY_ID_SQL, ps -> ps.setLong(1, id),
				rs -> rs.next() ? Optional.of(mapRow(rs)) : Optional.empty());
	}

	public Optional<User> findByEmail(String email) {
		return executeQuery(FIND_BY_EMAIL_SQL, ps -> ps.setString(1, email),
				rs -> rs.next() ? Optional.of(mapRow(rs)) : Optional.empty());
	}
	
	public Optional<String> findEmailById(Long id) {
	    return executeQuery(FIND_BY_ID_SQL,
	            ps -> ps.setLong(1, id),
	            rs -> {
	                if (rs.next()) {
	                    return Optional.ofNullable(rs.getString("email"));
	                }
	                return Optional.empty();
	            });
	}

	@Override
	public List<User> findAll() {
		return executeQuery(FIND_ALL_SQL, ps -> {
		}, rs -> {
			List<User> users = new ArrayList<>();
			while (rs.next()) {
				users.add(mapRow(rs));
			}
			return users;
		});
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

		Timestamp created = rs.getTimestamp("created_at");
		if (created != null) {
			user.setCreatedAt(created.toLocalDateTime());
		}

		Timestamp updated = rs.getTimestamp("updated_at");
		if (updated != null) {
			user.setUpdatedAt(updated.toLocalDateTime());
		}

		return user;
	}
}
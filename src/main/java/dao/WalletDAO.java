package dao;

import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entity.Wallet;

@ApplicationScoped
public class WalletDAO extends BaseDAO implements GenericDAO<Wallet, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO wallets (user_id, currency_code, balance) VALUES (?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE wallets SET balance=? WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM wallets WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM wallets WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM wallets ORDER BY id";

    private static final String FIND_BY_USER_SQL =
            "SELECT * FROM wallets WHERE user_id=?";

    private static final String FIND_BY_USER_AND_CURRENCY_SQL =
            "SELECT * FROM wallets WHERE user_id=? AND currency_code=?";

    @Override
    public Wallet save(Wallet wallet) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, wallet.getUserId());
            ps.setString(2, wallet.getCurrencyCode());
            ps.setBigDecimal(3, wallet.getBalance());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    wallet.setId(rs.getLong(1));
                }
            }

            return wallet;
        }
    }

    @Override
    public Wallet update(Wallet wallet) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setBigDecimal(1, wallet.getBalance());
            ps.setLong(2, wallet.getId());

            ps.executeUpdate();
            return wallet;
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
    public Optional<Wallet> findById(Long id) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Wallet> findAll() throws SQLException {
        List<Wallet> wallets = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                wallets.add(mapRow(rs));
            }
        }
        return wallets;
    }

    public List<Wallet> findByUserId(Long userId) throws SQLException {
        List<Wallet> wallets = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_USER_SQL)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wallets.add(mapRow(rs));
                }
            }
        }
        return wallets;
    }

    public Optional<Wallet> findByUserIdAndCurrency(Long userId, String currencyCode) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_USER_AND_CURRENCY_SQL)) {

            ps.setLong(1, userId);
            ps.setString(2, currencyCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    private Wallet mapRow(ResultSet rs) throws SQLException {
        Wallet wallet = new Wallet();
        wallet.setId(rs.getLong("id"));
        wallet.setUserId(rs.getLong("user_id"));
        wallet.setCurrencyCode(rs.getString("currency_code"));
        wallet.setBalance(rs.getBigDecimal("balance"));
        return wallet;
    }
}


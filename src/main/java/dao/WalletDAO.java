package dao;

import jakarta.enterprise.context.ApplicationScoped;
import entity.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WalletDAO extends BaseDAO implements GenericDAO<Wallet, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO wallets (user_id, currency_code, balance) VALUES (?, ?, ?) RETURNING id";

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
    public Wallet save(Wallet wallet) {
        Long id = executeQuery(
                INSERT_SQL,
                ps -> {
                    ps.setLong(1, wallet.getUserId());
                    ps.setString(2, wallet.getCurrencyCode());
                    ps.setBigDecimal(3, wallet.getBalance());
                },
                rs -> {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                    return null;
                }
        );

        wallet.setId(id);
        return wallet;
    }

    @Override
    public Wallet update(Wallet wallet) {
        executeUpdate(
                UPDATE_SQL,
                ps -> {
                    ps.setBigDecimal(1, wallet.getBalance());
                    ps.setLong(2, wallet.getId());
                }
        );
        return wallet;
    }

    @Override
    public void delete(Long id) {
        executeUpdate(
                DELETE_SQL,
                ps -> ps.setLong(1, id)
        );
    }

    @Override
    public Optional<Wallet> findById(Long id) {
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
    public List<Wallet> findAll() {
        return executeQuery(
                FIND_ALL_SQL,
                ps -> {},
                rs -> {
                    List<Wallet> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                    return list;
                }
        );
    }

    public List<Wallet> findByUserId(Long userId) {
        return executeQuery(
                FIND_BY_USER_SQL,
                ps -> ps.setLong(1, userId),
                rs -> {
                    List<Wallet> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                    return list;
                }
        );
    }

    public Optional<Wallet> findByUserIdAndCurrency(Long userId, String currencyCode) {
        return executeQuery(
                FIND_BY_USER_AND_CURRENCY_SQL,
                ps -> {
                    ps.setLong(1, userId);
                    ps.setString(2, currencyCode);
                },
                rs -> {
                    if (rs.next()) {
                        return Optional.of(mapRow(rs));
                    }
                    return Optional.empty();
                }
        );
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

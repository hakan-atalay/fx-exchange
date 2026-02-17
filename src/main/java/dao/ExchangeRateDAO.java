package dao;

import entity.ExchangeRate;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ExchangeRateDAO extends BaseDAO implements GenericDAO<ExchangeRate, Long> {

    private static final String INSERT_SQL =
            "INSERT INTO exchange_rates (base_currency_code,target_currency_code,rate,source) VALUES (?,?,?,?) RETURNING id";

    private static final String UPDATE_SQL =
            "UPDATE exchange_rates SET rate=?, source=?, fetched_at=CURRENT_TIMESTAMP WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM exchange_rates WHERE id=?";

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM exchange_rates WHERE id=?";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM exchange_rates ORDER BY id";

    @Override
    public ExchangeRate save(ExchangeRate rate) {
        Long id = executeQuery(
                INSERT_SQL,
                ps -> {
                    ps.setString(1, rate.getBaseCurrencyCode());
                    ps.setString(2, rate.getTargetCurrencyCode());
                    ps.setBigDecimal(3, rate.getRate());
                    ps.setString(4, rate.getSource());
                },
                rs -> {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                    return null;
                }
        );

        rate.setId(id);
        return rate;
    }

    @Override
    public ExchangeRate update(ExchangeRate rate) {
        executeUpdate(
                UPDATE_SQL,
                ps -> {
                    ps.setBigDecimal(1, rate.getRate());
                    ps.setString(2, rate.getSource());
                    ps.setLong(3, rate.getId());
                }
        );
        return rate;
    }

    @Override
    public void delete(Long id) {
        executeUpdate(
                DELETE_SQL,
                ps -> ps.setLong(1, id)
        );
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
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
    public List<ExchangeRate> findAll() {
        return executeQuery(
                FIND_ALL_SQL,
                ps -> {},
                rs -> {
                    List<ExchangeRate> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                    return list;
                }
        );
    }

    private ExchangeRate mapRow(ResultSet rs) throws SQLException {
        ExchangeRate rate = new ExchangeRate();
        rate.setId(rs.getLong("id"));
        rate.setBaseCurrencyCode(rs.getString("base_currency_code"));
        rate.setTargetCurrencyCode(rs.getString("target_currency_code"));
        rate.setRate(rs.getBigDecimal("rate"));
        rate.setSource(rs.getString("source"));

        if (rs.getTimestamp("fetched_at") != null) {
            rate.setFetchedAt(rs.getTimestamp("fetched_at").toLocalDateTime());
        }

        return rate;
    }
}

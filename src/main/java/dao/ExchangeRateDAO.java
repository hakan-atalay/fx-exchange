package dao;

import entity.ExchangeRate;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
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
    public ExchangeRate save(ExchangeRate rate) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, rate.getBaseCurrencyCode());
            ps.setString(2, rate.getTargetCurrencyCode());
            ps.setBigDecimal(3, rate.getRate());
            ps.setString(4, rate.getSource());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rate.setId(rs.getLong("id"));
            }
            return rate;
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate rate) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setBigDecimal(1, rate.getRate());
            ps.setString(2, rate.getSource());
            ps.setLong(3, rate.getId());

            ps.executeUpdate();
            return rate;
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
    public Optional<ExchangeRate> findById(Long id) throws Exception {
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
    public List<ExchangeRate> findAll() throws Exception {
        List<ExchangeRate> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private ExchangeRate mapRow(ResultSet rs) throws SQLException {
        ExchangeRate rate = new ExchangeRate();
        rate.setId(rs.getLong("id"));
        rate.setBaseCurrencyCode(rs.getString("base_currency_code"));
        rate.setTargetCurrencyCode(rs.getString("target_currency_code"));
        rate.setRate(rs.getBigDecimal("rate"));
        rate.setSource(rs.getString("source"));
        rate.setFetchedAt(rs.getTimestamp("fetched_at").toLocalDateTime());
        return rate;
    }
}

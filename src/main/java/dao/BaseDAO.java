package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;

import dao.support.SQLConsumer;
import dao.support.SQLFunction;
import exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public abstract class BaseDAO {

    @Inject
    private DataSource dataSource;

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    protected int executeUpdate(String sql, SQLConsumer<PreparedStatement> setter) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setter.accept(ps);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Database update error", e);
        }
    }

    protected <T> T executeQuery(String sql,
                                 SQLConsumer<PreparedStatement> setter,
                                 SQLFunction<ResultSet, T> extractor) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setter.accept(ps);

            try (ResultSet rs = ps.executeQuery()) {
                return extractor.apply(rs);
            }

        } catch (SQLException e) {
            throw new DAOException("Database query error", e);
        }
    }
}

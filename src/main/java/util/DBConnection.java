package util;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class DBConnection {

    private DataSource dataSource;

    @PostConstruct
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:/jdbc/FxExchangeDS");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Produces
    public DataSource produceDataSource() {
        return dataSource;
    }
}

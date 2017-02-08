package companyB.metrics.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JdbcSqlConnection
{
    @Value("${jdbc.username}")
    private  String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.driverManager}")
    private String driverClass;
    private final Logger LOGGER = LoggerFactory.getLogger(JdbcSqlConnection.class);

    public Connection connection() throws SQLException
    {
        try
        {
            Class.forName(driverClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            LOGGER.error(e.getMessage(),e);
            throw new SQLException(e.getMessage());
        }
        return DriverManager.getConnection(url, username,password);
    }
}

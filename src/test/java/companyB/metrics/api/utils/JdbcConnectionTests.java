package companyB.metrics.api.utils;

import companyB.metrics.api.TestBase;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;

public class JdbcConnectionTests extends TestBase
{

    @Test
    public void happyPath()
    {
        try
        {
            Connection connection = jdbcSqlConnection.connection();
            assertNotNull(connection);
            connection.close();
            connection = null;
        }
        catch (SQLException e)
        {
            fail(e.getMessage());
        }
    }
}

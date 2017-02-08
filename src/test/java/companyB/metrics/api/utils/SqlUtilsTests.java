package companyB.metrics.api.utils;

import companyB.metrics.api.TestBase;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SqlUtilsTests extends TestBase
{
    @Test
    public void testBaseException()
    {
        final String expected = "this,that,the other";
        final String actual = sqlUtils.handleException(new Exception(expected));
        assertEquals(expected, actual);
    }
    @Test
    public void testSqlException()
    {
        final List<String> errors = Arrays.asList("This","That","The Other");
        final SQLException sqlException = new SQLException("base");
        final String expected = "base-> This-> That-> The Other";
        errors.forEach((error)-> sqlException.setNextException(new SQLException(error)));
        final String actual = sqlUtils.handleException(sqlException);
        assertEquals(expected.trim(), actual);
    }
}

package companyB.metrics.api.utils;

import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class SqlUtils
{
    public String handleException(Exception e)
    {
        final StringBuilder stringBuilder = new StringBuilder(e.getMessage());
        if(e instanceof SQLException)
            ((SQLException)e).iterator().forEachRemaining((exception) ->{
                if(exception.getMessage()!=e.getMessage())stringBuilder.append(String.format("-> %s",exception.getMessage()));
            });
        return stringBuilder.toString();
    }
    public String handleSqlException(SQLException e)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        e.iterator().forEachRemaining((exception) -> stringBuilder.append(String.format("-> %s",exception.getMessage())));
        return stringBuilder.toString().trim();
    }
}

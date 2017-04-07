package companyB.metrics.api.data_access.impl.jdbc;

import com.google.gson.Gson;
import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.insert.InsertMetricEntryResponse;
import companyB.metrics.api.contract.list.ListMetricsResponse;
import companyB.metrics.api.data_access.MetricsDao;
import companyB.metrics.api.model.MetricEntry;
import companyB.metrics.api.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MetricsDaoJdbcImpl extends BaseMetricJdbcApiDataAccess implements MetricsDao
{
    @Autowired
    private DateUtils dateUtils;
    private Gson gson = new Gson();

    @Override
    @Cacheable(value = "metrics")
    public ListMetricsResponse list(String guid, String since, String until) throws SQLException
    {
        final ListMetricsResponse listMetricsResponse = new ListMetricsResponse();
        listMetricsResponse.setStatus(MetricsApiStatus.SUCCESS);
        final String sql = composeListMetricsSql(guid, since, until);
        try(
                final Connection connection = jdbcSqlConnection.connection();
                final Statement statement = connection.createStatement();
                final ResultSet resultSet = statement.executeQuery(sql))
        {
            while(resultSet.next())listMetricsResponse.getMetricsEntries().add(fromResultSet(resultSet));
        }
        finally
        {
            listMetricsResponse.setGuid(guid);
        }
        return listMetricsResponse;
    }


    @Override
    public InsertMetricEntryResponse insert(InsertMetricEntryRequest insertMetricEntryRequest) throws SQLException
    {
        final InsertMetricEntryResponse insertMetricEntryResponse = new InsertMetricEntryResponse();
        final String sql = composeMethodEntryInsertionSql(insertMetricEntryRequest);
        try(
                final Connection connection = jdbcSqlConnection.connection();
                final Statement statement = connection.createStatement())
        {
            statement.execute(sql);
            insertMetricEntryResponse.setStatus(MetricsApiStatus.SUCCESS);
        }
        finally
        {
            insertMetricEntryResponse.setGuid(insertMetricEntryRequest.getGuid());
        }
        return insertMetricEntryResponse;
    }

    private String composeListMetricsSql(String guid, String since, String until)
    {
        final Long sinceTimestamp = dateUtils.toTimestamp(since);
        final Long untilTimestamp = dateUtils.toTimestamp(until);
        return String.format("SELECT * FROM MetricEntry WHERE METRIC_GUID='%s' AND ENTRY_TIMESTAMP>=%s AND ENTRY_TIMESTAMP<=%s",
                guid, sinceTimestamp, untilTimestamp);
    }
    private String composeMethodEntryInsertionSql(InsertMetricEntryRequest insertMetricEntryRequest)
    {
        final Long timestampFromEntry = insertMetricEntryRequest.getMetricEntry().getEntryTimestamp();
        final String dateEnteredFromEntry = insertMetricEntryRequest.getMetricEntry().getEntryDate();
        final Long timestamp  = (null == timestampFromEntry) ? System.currentTimeMillis() : timestampFromEntry;
        final String dateEntered  = (null == dateEnteredFromEntry) ? dateUtils.fromTimestamp(timestamp) : dateEnteredFromEntry;
        final String attributes = gson.toJson(insertMetricEntryRequest.getMetricEntry().getAttributes());
        return String.format("INSERT INTO MetricEntry (ENTRY_TIMESTAMP,ENTRY_DATE,ATTRIBUTES,METRIC_GUID) VALUES (%s,'%s','%s','%s')",
                timestamp,dateEntered,attributes,insertMetricEntryRequest.getGuid());
    }

    private MetricEntry fromResultSet(ResultSet resultSet) throws SQLException
    {
        final MetricEntry metricEntry = new MetricEntry();
        final String attributes = resultSet.getString("ATTRIBUTES");
        Map<String,Object>attributesMapping = new HashMap<>();
        attributesMapping = gson.fromJson(attributes,attributesMapping.getClass());
        metricEntry.setAttributes(attributesMapping);
        metricEntry.setEntryTimestamp(resultSet.getLong("ENTRY_TIMESTAMP"));
        metricEntry.setEntryDate(resultSet.getString("ENTRY_DATE"));
        metricEntry.setId(resultSet.getLong("ID"));
        return metricEntry;
    }

}

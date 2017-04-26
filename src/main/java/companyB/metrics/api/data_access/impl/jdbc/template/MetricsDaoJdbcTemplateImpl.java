package companyB.metrics.api.data_access.impl.jdbc.template;

import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.data_access.MetricsDao;
import companyB.metrics.api.model.MetricEntry;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class MetricsDaoJdbcTemplateImpl extends BaseMetricJdbcTemplateApiDataAccess implements MetricsDao
{

    @Override
    public List<MetricEntry> list(String guid, String since, String until) throws SQLException
    {
        final String sql = sqlGenerator.listMetricsSql(guid, since, until);
        return jdbcTemplate.query(sql,rowSetMapperProvider.metricEntryRowMapper);
    }

    @Override
    public Boolean insert(InsertMetricEntryRequest insertMetricEntryRequest) throws SQLException
    {
        final String sql = sqlGenerator.insertMetricEntrySql(insertMetricEntryRequest);
        return jdbcTemplate.update(sql)> 0;
    }
}

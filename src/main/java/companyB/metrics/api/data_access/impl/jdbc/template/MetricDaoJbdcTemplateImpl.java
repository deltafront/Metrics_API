package companyB.metrics.api.data_access.impl.jdbc.template;

import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.data_access.MetricDao;
import companyB.metrics.api.model.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MetricDaoJbdcTemplateImpl extends BaseMetricJdbcTemplateApiDataAccess implements MetricDao
{
    @Autowired
    private CounterService counterService;
    private final String registeredMetricCount = "registered.metric.count";

    @Override
    public String register(RegisterMetricRequest registerMetricRequest) throws SQLException
    {
        final UUID uuid = UUID.randomUUID();
        final String guid = uuid.toString();
        final String sql = sqlGenerator.registerMetricSql(registerMetricRequest, guid);
        insertUpdateDelete(sql);
        counterService.increment(registeredMetricCount);
        return guid;
    }

    @Override
    public Boolean update(String guid, UpdateMetricRequest updateMetricRequest) throws SQLException
    {
        final String sql = sqlGenerator.updateMetricSql(guid, updateMetricRequest);
        return  insertUpdateDelete(sql) > 0;
    }


    @Override
    public Boolean delete(String guid) throws SQLException
    {
        final Boolean deleted = deleteMetricAndEntries(guid);
        if(deleted)counterService.decrement(registeredMetricCount);
        return deleted;
    }

    @Override
    public Metric get(String guid) throws SQLException
    {
        final String sql = sqlGenerator.getMetricSql(guid);
        final List<Metric>metrics = this.jdbcTemplate.query(sql,rowSetMapperProvider.metricRowMapper);
        return metrics.isEmpty() ? null : metrics.get(0);
    }

    @Override
    public Integer getCount() throws SQLException
    {
        LOGGER.debug("Getting total metrics count.");
        final AtomicInteger count = new AtomicInteger(0);
        final String sql = sqlGenerator.getMetricCountSql();
        count.set(this.jdbcTemplate.queryForObject(sql,Integer.class));
        LOGGER.debug(String.format("Returning a count of '%d'.",count.get()));
        return count.get();
    }
    @Override
    public void clear()
    {
        jdbcTemplate.update("DELETE FROM MetricEntry WHERE ID > -1");
        jdbcTemplate.update("DELETE FROM Metric WHERE ID > -1");
    }

    @Override
    public List<String> list() throws SQLException
    {
        LOGGER.debug("Fetching all of the Guids of registered metrics.");
        final List<String>guids = new LinkedList<>();
        final String sql = sqlGenerator.listMetricsSql();
        final SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(sql);
        while(rowSet.next())guids.add(rowSet.getString("GUID"));
        return guids;
    }

    private Boolean deleteMetricAndEntries(String guid) throws SQLException
    {
        deleteMetricEntries(guid);
        return deleteRegisteredMetric(guid) > 0;
    }
    private Integer deleteRegisteredMetric(String guid) throws SQLException
    {
        final String deleteMetricSql = sqlGenerator.deleteMetricSql(guid);
        return insertUpdateDelete(deleteMetricSql);
    }

    private Integer deleteMetricEntries(String guid) throws SQLException
    {
        final String deleteMetricEntriesSql = sqlGenerator.deleteMetricEntriesSql(guid);
        return insertUpdateDelete(deleteMetricEntriesSql);
    }
}


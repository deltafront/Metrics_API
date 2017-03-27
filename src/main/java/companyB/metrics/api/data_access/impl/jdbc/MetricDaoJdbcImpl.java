package companyB.metrics.api.data_access.impl.jdbc;

import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricResponse;
import companyB.metrics.api.data_access.MetricDao;
import companyB.metrics.api.model.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MetricDaoJdbcImpl extends BaseMetricJdbcApiDataAccess implements MetricDao
{
    @Autowired
    private CounterService counterService;
    private final String registeredMetricCount = "registered.metric.count";
    @Override
    public RegisterMetricResponse register(RegisterMetricRequest registerMetricRequest) throws SQLException
    {
        LOGGER.debug("Registering new Metric.");
        final RegisterMetricResponse registerMetricResponse = new RegisterMetricResponse();
        registerMetricResponse.setStatus(MetricsApiStatus.SUCCESS);
        final UUID uuid = UUID.randomUUID();
        final String guid = uuid.toString();
        final String sql = composeRegisterMetricSql(registerMetricRequest, guid);
        try(
                final Connection connection = jdbcSqlConnection.connection();
                final Statement statement = connection.createStatement())
        {
            statement.execute(sql);
            registerMetricResponse.setGuid(guid);
            counterService.increment(registeredMetricCount);
        }
        finally
        {
            registerMetricResponse.setName(registerMetricRequest.getName());
        }

        return registerMetricResponse;
    }
    private String composeRegisterMetricSql(RegisterMetricRequest registerMetricRequest, String guid)
    {
        return String.format("INSERT INTO Metric(NAME,CONTACT_NAME,CONTACT_EMAIL,TIMEZONE,GUID) VALUES ('%s','%s','%s','%s', '%s')",registerMetricRequest.getName(),
                registerMetricRequest.getContactName(), registerMetricRequest.getContactEmail(),
                registerMetricRequest.getTimezone(),guid);
    }

    @Override
    public UpdateMetricResponse update(String guid, UpdateMetricRequest updateMetricRequest) throws SQLException
    {
        LOGGER.debug("Updating existing Metric");
        final UpdateMetricResponse updateMetricResponse = new UpdateMetricResponse();
        updateMetricResponse.setStatus(MetricsApiStatus.SUCCESS);
        try(final Connection connection = jdbcSqlConnection.connection())
        {
            final String sql = String.format("UPDATE Metric SET CONTACT_NAME='%s', NAME='%s', CONTACT_EMAIL='%s',TIMEZONE='%s' WHERE GUID='%s'",
                   updateMetricRequest.getContactName(), updateMetricRequest.getName(), updateMetricRequest.getContactEmail(),updateMetricRequest.getTimezone(),
                   guid);
            final int updated = connection.createStatement().executeUpdate(sql);
            updateMetricResponse.setStatus((0==updated) ? MetricsApiStatus.FAILURE : MetricsApiStatus.SUCCESS);
        }
        finally
        {
            updateMetricResponse.setGuid(guid);
            updateMetricResponse.setName(updateMetricRequest.getName());
        }
        return updateMetricResponse;
    }

    @Override
    public DeleteMetricResponse delete(String guid) throws SQLException
    {
        LOGGER.debug("Deleting existing Metric");
        final DeleteMetricResponse deleteMetricResponse = new DeleteMetricResponse();
        deleteMetricResponse.setGuid(guid);
        deleteMetricResponse.setStatus(MetricsApiStatus.SUCCESS);
        try(
                final Connection connection = jdbcSqlConnection.connection();
                final Statement statement = connection.createStatement())
        {
            final String deleteMetricSql = String.format("DELETE FROM Metric WHERE GUID='%s'",guid);
            final String deleteMetricEntriesSql = String.format("DELETE FROM MetricEntry WHERE METRIC_GUID='%s'",guid);
            final int deleted = statement.executeUpdate(deleteMetricEntriesSql);
            final int metricDeleted = statement.executeUpdate(deleteMetricSql);
            String message;
            if(0!= metricDeleted)message = String.format("%1$d entries for Metric Guid '%2$s' have been deleted. Metric Guid '%2$s' has been deleted.", deleted, guid);
            else message = String.format("Metric Guid '%s' was not deleted.",guid);
            deleteMetricResponse.setStatus((0==metricDeleted) ? MetricsApiStatus.FAILURE : MetricsApiStatus.SUCCESS);
            deleteMetricResponse.setMessage(message);
            counterService.decrement(registeredMetricCount);
        }
        finally
        {
            deleteMetricResponse.setGuid(guid);
        }
        return deleteMetricResponse;
    }

    @Override
    public GetMetricResponse get(String guid) throws SQLException
    {
        LOGGER.debug("Fetching existing Metric");
        final GetMetricResponse getMetricResponse = new GetMetricResponse();
        try(final Connection connection = jdbcSqlConnection.connection())
        {
            final String sql = String.format("SELECT * FROM Metric WHERE GUID='%s'", guid);
            final ResultSet resultSet = connection.createStatement().executeQuery(sql);
            Metric metric = null;
            while (resultSet.next()) metric = registeredMetricFromResultSet(resultSet);
            getMetricResponse.setMetric(metric);
            getMetricResponse.setStatus((null== metric)? MetricsApiStatus.FAILURE : MetricsApiStatus.SUCCESS);
            if(null != metric)getMetricResponse.setName(metric.getName());
        }
        finally
        {
            getMetricResponse.setGuid(guid);
        }
        return getMetricResponse;
    }

    @Override
    public Integer getCount() throws SQLException
    {
        LOGGER.debug("Getting total metrics count.");
        final AtomicInteger count = new AtomicInteger(0);
        try(final Connection connection = jdbcSqlConnection.connection())
        {
            final String sql = "SELECT COUNT(GUID) FROM Metric";
            final ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) count.set(resultSet.getInt(1));
        }
        LOGGER.debug(String.format("Returning a count of '%d'.",count.get()));
        return count.get();

    }

    @Override
    public List<String> list() throws SQLException
    {
        LOGGER.debug("Fetching all of the Guids of registered metrics.");
        final List<String>guids = new LinkedList<>();
        try(final Connection connection = jdbcSqlConnection.connection())
        {
            final String sql = "SELECT GUID FROM Metric WHERE id !=-1";
            final ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) guids.add(resultSet.getString("GUID"));
        }
        return guids;
    }


    private Metric registeredMetricFromResultSet(ResultSet resultSet)
    {
        final Metric metric = new Metric();
        try
        {
            metric.setContactEmail(resultSet.getString("CONTACT_EMAIL"));
            metric.setContactName(resultSet.getString("CONTACT_NAME"));
            metric.setName(resultSet.getString("NAME"));
            metric.setTimezone(resultSet.getString("TIMEZONE"));
            metric.setGuid(resultSet.getString("GUID"));
        }
        catch (SQLException e)
        {
            LOGGER.error(sqlUtils.handleSqlException(e));
        }
        return metric;
    }

}

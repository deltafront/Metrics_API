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
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

@Repository
public class MetricDaoJdbcImpl extends BaseMetricJdbcApiDataAccess implements MetricDao
{
    @Override
    public RegisterMetricResponse register(RegisterMetricRequest registerMetricRequest) throws SQLException
    {
        LOGGER.debug("Registering new Metric.");
        final RegisterMetricResponse registerMetricResponse = new RegisterMetricResponse();
        registerMetricResponse.setStatus(MetricsApiStatus.SUCCESS);
        try(final Connection connection = jdbcSqlConnection.connection())
        {
            final UUID uuid = UUID.randomUUID();
            final String guid = uuid.toString();
            final String sql = String.format("INSERT INTO Metric(NAME,CONTACT_NAME,CONTACT_EMAIL,TIMEZONE,GUID) VALUES ('%s','%s','%s','%s', '%s')",registerMetricRequest.getName(),
                    registerMetricRequest.getContactName(), registerMetricRequest.getContactEmail(),
                    registerMetricRequest.getTimezone(),guid);
            connection.createStatement().execute(sql);
            registerMetricResponse.setGuid(guid);
        }
        finally
        {
            registerMetricResponse.setName(registerMetricRequest.getName());
        }

        return registerMetricResponse;
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
        try(final Connection connection = jdbcSqlConnection.connection())
        {
            final String deleteMetricSql = String.format("DELETE FROM Metric WHERE GUID='%s'",guid);
            final String deleteMetricEntriesSql = String.format("DELETE FROM MetricEntry WHERE METRIC_GUID='%s'",guid);
            final Statement statement = connection.createStatement();
            final int deleted = statement.executeUpdate(deleteMetricEntriesSql);
            final int metricDeleted = statement.executeUpdate(deleteMetricSql);
            String message;
            if(0!= metricDeleted)message = String.format("%1$d entries for Metric Guid '%2$s' have been deleted. Metric Guid '%2$s' has been deleted.", deleted, guid);
            else message = String.format("Metric Guid '%s' was not deleted.",guid);
            deleteMetricResponse.setStatus((0==metricDeleted) ? MetricsApiStatus.FAILURE : MetricsApiStatus.SUCCESS);
            deleteMetricResponse.setMessage(message);
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

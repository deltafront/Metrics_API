package companyB.metrics.api.data_access;

import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.model.Metric;

import java.sql.SQLException;
import java.util.List;

public interface MetricDao
{
    String register(RegisterMetricRequest registerMetricRequest) throws SQLException;
    Boolean update(String guid, UpdateMetricRequest updateMetricRequest) throws SQLException;
    Boolean delete(String guid) throws SQLException;
    Metric get(String guid) throws SQLException;
    Integer getCount() throws SQLException;
    List<String> list() throws SQLException;
    void clear();
}

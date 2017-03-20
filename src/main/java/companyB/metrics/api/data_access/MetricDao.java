package companyB.metrics.api.data_access;

import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricResponse;

import java.sql.SQLException;
import java.util.List;

public interface MetricDao
{
    RegisterMetricResponse register(RegisterMetricRequest registerMetricRequest) throws SQLException;
    UpdateMetricResponse update(String guid, UpdateMetricRequest updateMetricRequest) throws SQLException;
    DeleteMetricResponse delete(String guid) throws SQLException;
    GetMetricResponse get(String guid) throws SQLException;
    Integer getCount() throws SQLException;
    List<String> list() throws SQLException;
}

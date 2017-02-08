package companyB.metrics.api.data_access;

import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricResponse;

import java.sql.SQLException;

public interface MetricDao
{
    public RegisterMetricResponse register(RegisterMetricRequest registerMetricRequest) throws SQLException;
    public UpdateMetricResponse update(String guid, UpdateMetricRequest updateMetricRequest) throws SQLException;
    public DeleteMetricResponse delete(String guid) throws SQLException;
    public GetMetricResponse get(String guid) throws SQLException;
}

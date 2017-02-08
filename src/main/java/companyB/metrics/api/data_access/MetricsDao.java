package companyB.metrics.api.data_access;


import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.insert.InsertMetricEntryResponse;
import companyB.metrics.api.contract.list.ListMetricsResponse;

import java.sql.SQLException;

public interface MetricsDao
{
    public ListMetricsResponse list(String guid, String since, String until) throws SQLException;
    public InsertMetricEntryResponse insert(InsertMetricEntryRequest insertMetricEntryRequest) throws SQLException;
}

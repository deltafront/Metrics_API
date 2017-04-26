package companyB.metrics.api.data_access;


import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.model.MetricEntry;

import java.sql.SQLException;
import java.util.List;

public interface MetricsDao
{
    List<MetricEntry> list(String guid, String since, String until) throws SQLException;
    Boolean insert(InsertMetricEntryRequest insertMetricEntryRequest) throws SQLException;
}

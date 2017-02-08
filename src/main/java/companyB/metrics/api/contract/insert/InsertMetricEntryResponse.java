package companyB.metrics.api.contract.insert;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.contract.MetricsApiStatus;

public class InsertMetricEntryResponse extends BaseMetricsResponse
{

    @Override
    public String getMessage()
    {
        return String.format("Metric Entry entered for Metric Guid %s ? %B.", this.guid, (this.status.equals(MetricsApiStatus.SUCCESS)));
    }
}

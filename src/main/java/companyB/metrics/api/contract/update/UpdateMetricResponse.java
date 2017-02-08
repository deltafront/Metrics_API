package companyB.metrics.api.contract.update;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.contract.MetricsApiStatus;

public class UpdateMetricResponse extends BaseMetricsResponse
{

    @Override
    public String getMessage()
    {
        return String.format("Metric Guid '%s' updated? %b %s", this.guid, status.equals(MetricsApiStatus.SUCCESS),message).trim();
    }
}

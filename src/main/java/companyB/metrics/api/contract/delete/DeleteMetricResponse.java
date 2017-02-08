package companyB.metrics.api.contract.delete;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.contract.MetricsApiStatus;

public class DeleteMetricResponse extends BaseMetricsResponse
{

    @Override
    public String getMessage()
    {
        return String.format("Metric Guid '%s' deleted? %b. %s",
                this.guid, this.status.equals(MetricsApiStatus.SUCCESS),message).trim();
    }
}

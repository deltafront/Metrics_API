package companyB.metrics.api.contract.register;


import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.contract.MetricsApiStatus;

public class RegisterMetricResponse extends BaseMetricsResponse
{
    @Override
    public String getMessage()
    {
        return String.format("Metric name '%s' registration successful? %B. %s",
                this.name, this.status.equals(MetricsApiStatus.SUCCESS),this.message).trim();
    }
}

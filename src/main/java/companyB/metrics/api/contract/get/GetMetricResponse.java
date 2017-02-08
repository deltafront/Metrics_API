package companyB.metrics.api.contract.get;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.model.Metric;

public class GetMetricResponse extends BaseMetricsResponse
{
    private Metric metric;


    public Metric getMetric()
    {
        return metric;
    }

    public void setMetric(Metric metric)
    {
        this.metric = metric;
    }
    public void setMetric(String metricJson)
    {
        this.metric = gson.fromJson(metricJson, Metric.class);
    }
    @Override
    public String getMessage()
    {
        return String.format("Metric Guid '%s' found? %B. %s",
                this.guid, this.status.equals(MetricsApiStatus.SUCCESS), this.message).trim();
    }
}

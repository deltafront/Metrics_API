package companyB.metrics.api.contract.list;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.model.MetricEntry;

import java.util.LinkedList;
import java.util.List;


public class ListMetricsResponse extends BaseMetricsResponse
{
    private String since;
    private String until;
    private List<MetricEntry>metricsEntries = new LinkedList<>();


    public String getSince()
    {
        return since;
    }

    public void setSince(String since)
    {
        this.since = since;
    }

    public String getUntil()
    {
        return until;
    }

    public void setUntil(String until)
    {
        this.until = until;
    }

    public List<MetricEntry> getMetricsEntries()
    {
        return metricsEntries;
    }

    public void setMetricsEntries(List<MetricEntry> metricsEntries)
    {
        this.metricsEntries = metricsEntries;
    }

    public void setMetricsEntries(String metricsJson)
    {
        final List<MetricEntry>metrics = new LinkedList<>();
        this.metricsEntries = gson.fromJson(metricsJson,metrics.getClass());
    }

    @Override
    public String getMessage()
    {
        return String.format("Found %d Metrics Entries for Metrics Guid %s (From '%s' To '%s')\n%s.",
                metricsEntries.size(),this.guid,since,until,message);
    }
}

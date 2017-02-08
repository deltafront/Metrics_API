package companyB.metrics.api.contract.insert;

import companyB.metrics.api.contract.BaseMetricsRequest;
import companyB.metrics.api.model.MetricEntry;
import org.apache.commons.lang3.Validate;

import java.util.Map;

public class InsertMetricEntryRequest extends BaseMetricsRequest
{
    private String guid;
    private MetricEntry metricEntry;

    public String getGuid()
    {
        return guid;
    }

    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    public MetricEntry getMetricEntry()
    {
        return metricEntry;
    }

    public void setMetricEntry(MetricEntry metricEntry)
    {
        this.metricEntry = metricEntry;
    }

    @Override
    public void validate()
    {
        Validate.notBlank(guid,"Metric Guid must be provided.");
        Validate.notNull(metricEntry,"Metric Entry must be provided.");
        final Map<String,Object> attributes = metricEntry.getAttributes();
        Validate.notNull(attributes,"Attribute mapping must be provided.");
        metricEntry.getAttributes().forEach((key,value) -> Validate.notBlank(key,String.format("Null key provided for value '%s'.",value)));
    }
}

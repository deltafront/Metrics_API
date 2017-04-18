package companyB.metrics.api.cache;

import companyB.metrics.api.model.MetricEntry;

import java.util.List;

public interface MetricsCache
{
    List<MetricEntry> get(Object...keys);
    void set(List<MetricEntry>metricEntries, Object...keys);

    default String constructKey(Object...args)
    {
        final StringBuilder key = new StringBuilder();
        for(final Object arg : args)key.append(String.format("%s:",arg));
        final Integer lastIndex = key.lastIndexOf(":");
        return key.substring(0,lastIndex);
    }
}

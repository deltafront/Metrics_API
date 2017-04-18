package companyB.metrics.api.cache.impl.null_default;

import companyB.metrics.api.cache.MetricsCache;
import companyB.metrics.api.model.MetricEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricsCacheNullDefaultImpl implements MetricsCache
{
    @Override
    public List<MetricEntry> get(Object...keys)
    {
        return null;
    }

    @Override
    public void set(List<MetricEntry> metricEntries, Object... keys)
    {
        //do nothing
    }
}

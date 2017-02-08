package companyB.metrics.api.cache.impl;

import companyB.metrics.api.cache.MetricsCache;
import companyB.metrics.api.model.Metric;

import java.util.List;

/**
 * Created by chburrell on 2/7/17.
 */
public class MetricsCacheNullImpl implements MetricsCache
{
    @Override
    public List<Metric> get(String id)
    {
        return null;
    }

    @Override
    public Boolean insert(List<Metric> object)
    {
        return true;
    }
}

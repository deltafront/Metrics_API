package companyB.metrics.api.cache;

public interface MetricsApiCache<CachedType>
{
    public CachedType get(String id);
    public Boolean insert(CachedType object);
}

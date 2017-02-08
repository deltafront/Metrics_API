package companyB.metrics.api.contract;

import org.apache.commons.lang3.Validate;

public abstract class BaseMetricsRequest
{
    public abstract void validate();
    protected void validate(String name, Object value)
    {
        final String message = String.format("Value expected for field '%s'.",name);
        if(value instanceof String) Validate.notBlank((String)value,message);
        else Validate.notNull(value,message);
    }
}

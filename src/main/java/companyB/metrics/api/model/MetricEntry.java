package companyB.metrics.api.model;

import java.util.HashMap;
import java.util.Map;

public class MetricEntry
{
    private Map<String,Object> attributes = new HashMap<>();
    private String entryDate;
    private Long entryTimestamp;
    private Long id;

    public Map<String,Object> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(Map<String,Object> attributes)
    {
        this.attributes = attributes;
    }

    public String getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(String entryDate)
    {
        this.entryDate = entryDate;
    }

    public Long getEntryTimestamp()
    {
        return entryTimestamp;
    }

    public void setEntryTimestamp(Long entryTimestamp)
    {
        this.entryTimestamp = entryTimestamp;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}

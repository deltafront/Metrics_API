package companyB.metrics.api.contract;


import com.google.gson.Gson;

public abstract class BaseMetricsResponse
{
    protected String name;
    protected String guid;
    protected MetricsApiStatus status = MetricsApiStatus.SUCCESS;
    protected String message = "";
    protected final Gson gson = new Gson();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGuid()
    {
        return guid;
    }

    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    public MetricsApiStatus getStatus()
    {
        return status;
    }

    public void setStatus(MetricsApiStatus status)
    {
        this.status = status;
    }

    public abstract String getMessage();

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String toString()
    {
        return String.format("Status: %s Message: %s", status.name(),message);
    }

}

package companyB.metrics.api.contract.register;


import companyB.metrics.api.contract.BaseMetricsRequest;

public class RegisterMetricRequest extends BaseMetricsRequest
{
    protected String name;
    protected String timezone;
    protected String contactName;
    protected String contactEmail;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    @Override
    public void validate()
    {
        super.validate("Metric Name",this.name);
        super.validate("Timezone",this.timezone);
        super.validate("Contact Email",this.contactEmail);
        super.validate("Contact Name",this.contactName);
    }
}

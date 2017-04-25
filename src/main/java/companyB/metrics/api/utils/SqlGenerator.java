package companyB.metrics.api.utils;

import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import org.springframework.stereotype.Component;

@Component
public class SqlGenerator
{
    public String registerMetricSql(RegisterMetricRequest registerMetricRequest, String guid)
    {
        return String.format("INSERT INTO Metric(NAME,CONTACT_NAME,CONTACT_EMAIL,TIMEZONE,GUID) VALUES ('%s','%s','%s','%s', '%s')",registerMetricRequest.getName(),
                registerMetricRequest.getContactName(), registerMetricRequest.getContactEmail(),
                registerMetricRequest.getTimezone(),guid);
    }
    public String updateMetricSql(String guid, UpdateMetricRequest updateMetricRequest)
    {
        return String.format("UPDATE Metric SET CONTACT_NAME='%s', NAME='%s', CONTACT_EMAIL='%s',TIMEZONE='%s' WHERE GUID='%s'",
                updateMetricRequest.getContactName(), updateMetricRequest.getName(), updateMetricRequest.getContactEmail(),updateMetricRequest.getTimezone(),
                guid);
    }
    public String getMetricSql(String guid)
    {
       return String.format("SELECT * FROM Metric WHERE GUID='%s'", guid);
    }
    public String getMetricCountSql()
    {
        return "SELECT COUNT(GUID) FROM Metric";
    }
    public String listMetricsSql()
    {
        return "SELECT GUID FROM Metric WHERE id !=-1";
    }
    public String deleteMetricSql(String guid)
    {
        return String.format("DELETE FROM Metric WHERE GUID='%s'",guid);
    }
    public String deleteMetricEntriesSql(String guid)
    {
        return String.format("DELETE FROM MetricEntry WHERE METRIC_GUID='%s'",guid);
    }
}

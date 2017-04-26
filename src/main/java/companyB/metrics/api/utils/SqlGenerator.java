package companyB.metrics.api.utils;

import com.google.gson.Gson;
import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqlGenerator
{
    @Autowired
    private DateUtils dateUtils;
    private Gson gson = new Gson();

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
    public String listMetricsSql(String guid, String since, String until)
    {
        final Long sinceTimestamp = dateUtils.toTimestamp(since);
        final Long untilTimestamp = dateUtils.toTimestamp(until);
        return String.format("SELECT * FROM MetricEntry WHERE METRIC_GUID='%s' AND ENTRY_TIMESTAMP>=%s AND ENTRY_TIMESTAMP<=%s",
                guid, sinceTimestamp, untilTimestamp);
    }
    public String insertMetricEntrySql(InsertMetricEntryRequest insertMetricEntryRequest)
    {
        final Long timestampFromEntry = insertMetricEntryRequest.getMetricEntry().getEntryTimestamp();
        final String dateEnteredFromEntry = insertMetricEntryRequest.getMetricEntry().getEntryDate();
        final Long timestamp  = (null == timestampFromEntry) ? System.currentTimeMillis() : timestampFromEntry;
        final String dateEntered  = (null == dateEnteredFromEntry) ? dateUtils.fromTimestamp(timestamp) : dateEnteredFromEntry;
        final String attributes = gson.toJson(insertMetricEntryRequest.getMetricEntry().getAttributes());
        return String.format("INSERT INTO MetricEntry (ENTRY_TIMESTAMP,ENTRY_DATE,ATTRIBUTES,METRIC_GUID) VALUES (%s,'%s','%s','%s')",
                timestamp,dateEntered,attributes,insertMetricEntryRequest.getGuid());
    }
}

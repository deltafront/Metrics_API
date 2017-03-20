package companyB.metrics.api.mock;


import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.insert.InsertMetricEntryResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.data_access.MetricDao;
import companyB.metrics.api.data_access.MetricsDao;
import companyB.metrics.api.model.MetricEntry;
import companyB.metrics.api.utils.DateUtils;
import companyB.metrics.api.utils.SqlUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class MockDatasetService
{
    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private SqlUtils sqlUtils;

    @Autowired
    private MetricDao metricDao;

    @Autowired
    private MetricsDao metricsDao;

    @Value("${default.datetime.timezone}")
    private String defaultTimeZone;

    private final Random random = new Random();

    private String MOCK_GUID;

    public DeleteMetricResponse clear()
    {
        DeleteMetricResponse deleteMetricResponse = new DeleteMetricResponse();
        deleteMetricResponse.setGuid(MOCK_GUID);
        if(null != MOCK_GUID)
        {
            try
            {
                deleteMetricResponse = metricDao.delete(MOCK_GUID);
            }
            catch (SQLException e)
            {
                sqlUtils.handleSqlException(e);
                throw new RuntimeException(e);
            }
        }
        return deleteMetricResponse;

    }
    public RegisterMetricResponse mock()
    {
        final RegisterMetricResponse registerMetricResponse = registerMetric();
        insertEntries(registerMetricResponse.getGuid());
        return registerMetricResponse;
    }

    private void insertEntries(String guid)
    {
        final List<MetricEntry>metricEntries = getMetricEntries();
        metricEntries.forEach((metricEntry -> {
            final InsertMetricEntryRequest insertMetricEntryRequest = getInsertMetricEntryRequest(metricEntry,guid);
            insertMetricsEntry(insertMetricEntryRequest);
        }));
    }
    private RegisterMetricResponse registerMetric()
    {
        RegisterMetricResponse registerMetricResponse;
        try
        {
            if(null != MOCK_GUID)
                metricDao.delete(MOCK_GUID);
            final RegisterMetricRequest registerMetricRequest = getRegisterMetricRequest();
            registerMetricResponse = metricDao.register(registerMetricRequest);
            if(registerMetricResponse.getStatus().equals(MetricsApiStatus.SUCCESS))
                MOCK_GUID = registerMetricResponse.getGuid();
        }
        catch (SQLException e)
        {
            sqlUtils.handleSqlException(e);
            throw new RuntimeException(e);
        }
        return registerMetricResponse;

    }

    private RegisterMetricRequest getRegisterMetricRequest()
    {
        final RegisterMetricRequest registerMetricRequest = new RegisterMetricRequest();
        registerMetricRequest.setName("MOCK_DATA");
        registerMetricRequest.setContactName("MOCK");
        registerMetricRequest.setContactEmail("MOCK@MOCK.MOCK");
        registerMetricRequest.setTimezone(defaultTimeZone);
        return registerMetricRequest;
    }
    private InsertMetricEntryRequest getInsertMetricEntryRequest(MetricEntry metricEntry, String guid)
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setMetricEntry(metricEntry);
        insertMetricEntryRequest.setGuid(guid);
        return insertMetricEntryRequest;
    }
    private void insertMetricsEntry(InsertMetricEntryRequest  insertMetricEntryRequest)
    {
        try
        {
            InsertMetricEntryResponse insertMetricEntryResponse = metricsDao.insert(insertMetricEntryRequest);
            if(null == insertMetricEntryResponse || insertMetricEntryResponse.getStatus().equals(MetricsApiStatus.FAILURE))
            {
                final String message = "Metric Entry was not inserted.";
                insertMetricEntryResponse.getMessage().concat(message);
                throw new RuntimeException(message);
            }

        }
        catch (SQLException e)
        {
            sqlUtils.handleSqlException(e);
            throw new RuntimeException(e);
        }
    }

    private List<MetricEntry>getMetricEntries()
    {
        final List<MetricEntry>metricEntries = new LinkedList<>();
        getDates().forEach((date)->
                metricEntries.add(metricEntry(date)));
        return metricEntries;
    }

    private List<DateTime> getDates()
    {
        final List<DateTime>dates = new LinkedList<>();
        IntStream.rangeClosed(1,31).forEach((day)->
                IntStream.rangeClosed(0,23).forEach((hour) ->
                        Arrays.asList(0,30).forEach((minute)->
                        dates.add(new DateTime().withYear(2000)
                                .withMonthOfYear(8)
                                .withDayOfMonth(day)
                                .withHourOfDay(hour)
                                .withMinuteOfHour(minute)))));
        return dates;
    }

    private MetricEntry metricEntry(DateTime dateTime)
    {
        final MetricEntry metricEntry = new MetricEntry();
        metricEntry.setAttributes(attributes());
        metricEntry.setEntryDate(dateUtils.fromTimestamp(dateTime.getMillis()));
        metricEntry.setEntryTimestamp(dateTime.getMillis());
        return metricEntry;
    }
    private Map<String,Object>attributes()
    {
        final Map<String,Object>values = new HashMap<>();
        Arrays.asList("All","Foo","Bar").forEach((value)->
                values.put(value,random.nextInt(random.nextInt())));
        return values;
    }

}

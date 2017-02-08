package companyB.metrics.api.metrics;

import companyB.metrics.api.TestBase;
import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.insert.InsertMetricEntryResponse;
import companyB.metrics.api.contract.list.ListMetricsResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.model.MetricEntry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MetricsTests extends TestBase
{
    @Value("${default.datetime.timezone}")
    private String defaultTimeZone;

    @Value("${default.datetime.format}")
    private String dateTimeFormat;

    private String guid;
    private Integer upper = 15;
    private Integer sinceDay = 2;
    private Integer untilDay = 6;
    private Long sinceDayTimestamp;
    private Long untilDayTimestamp;
    private DateTimeFormatter dateTimeFormatter;
    private Map<String,Object>expectedAttributes;
    @Before
    public void before()
    {
        guid = insertMetricEntry();
        expectedAttributes = getMapping();
        final List<InsertMetricEntryRequest> insertMetricEntryRequests = getMetricEntryRequests(guid);
        insertMetricEntries(insertMetricEntryRequests);
        sinceDayTimestamp = getTimestamp(sinceDay);
        untilDayTimestamp = getTimestamp(untilDay);
        dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);
    }
    @After
    public void after()
    {
        final DeleteMetricResponse deleteMetricResponse = metricApiService.delete(guid);
        assertNotNull(deleteMetricResponse);
        assertEquals(MetricsApiStatus.SUCCESS, deleteMetricResponse.getStatus());
        final ListMetricsResponse listMetricsResponse = metricApiService.list(guid,null,null);
        assertNotNull(listMetricsResponse);
        assertEquals(0,listMetricsResponse.getMetricsEntries().size());
    }
    @Test
    public void getSince()
    {
        final String since = getDateFromTimestamp(sinceDayTimestamp);
        doTest(guid,since,null,(upper - sinceDay));
    }
    @Test
    public void getUntil()
    {
        final String until = getDateFromTimestamp(untilDayTimestamp);
        doTest(guid,null,until,untilDay);
    }
    @Test
    public void getSinceUntil()
    {
        final String since = getDateFromTimestamp(sinceDayTimestamp);
        final String until = getDateFromTimestamp(untilDayTimestamp);
        doTest(guid,since,until,untilDay - sinceDay);
    }
    @Test
    public void nullMetricEntry()
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setGuid(guid);
        insertMetricEntryRequest.setMetricEntry(null);
        doFailingTest(insertMetricEntryRequest);
    }
    @Test
    public void nullGuid()
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setGuid(null);
        insertMetricEntryRequest.setMetricEntry(new MetricEntry());
        doFailingTest(insertMetricEntryRequest);
    }
    @Test
    public void emptyStringGuid()
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setGuid("");
        insertMetricEntryRequest.setMetricEntry(new MetricEntry());
        doFailingTest(insertMetricEntryRequest);
    }
    @Test
    public void nullKey()
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setGuid(guid);
        insertMetricEntryRequest.setMetricEntry(new MetricEntry());
        insertMetricEntryRequest.getMetricEntry().getAttributes().put(null,"Foo");
        doFailingTest(insertMetricEntryRequest);
    }
    @Test
    public void emptyStringKey()
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setGuid(guid);
        insertMetricEntryRequest.setMetricEntry(new MetricEntry());
        insertMetricEntryRequest.getMetricEntry().getAttributes().put("","Foo");
        doFailingTest(insertMetricEntryRequest);
    }
    @Test
    public void validDateTimeFormatColon()
    {
        validDateTimeFormat("1969-08-03T15:15:15:123");
    }
    @Test
    public void validDateTimeFormatPeriod()
    {
        validDateTimeFormat("1969-08-03T15:15:15.123");
    }
    @Test
    public void invalidDateTime()
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setGuid(guid);
        insertMetricEntryRequest.setMetricEntry(new MetricEntry());
        insertMetricEntryRequest.getMetricEntry().setEntryDate("03 August 1969 3:15PM");
        doFailingTest(insertMetricEntryRequest);
    }
    private void validDateTimeFormat(String dateTime)
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setGuid(guid);
        insertMetricEntryRequest.setMetricEntry(new MetricEntry());
        insertMetricEntryRequest.getMetricEntry().setEntryDate(dateTime);
        final InsertMetricEntryResponse insertMetricEntryResponse = metricApiService.insert(insertMetricEntryRequest);
        assertNotNull(insertMetricEntryResponse);
        assertEquals(MetricsApiStatus.SUCCESS, insertMetricEntryResponse.getStatus());
    }
    private void doFailingTest(InsertMetricEntryRequest insertMetricEntryRequest)
    {
        try
        {
            final InsertMetricEntryResponse insertMetricEntryResponse = metricApiService.insert(insertMetricEntryRequest);
            assertNotNull(insertMetricEntryResponse);
            assertEquals(MetricsApiStatus.FAILURE, insertMetricEntryResponse.getStatus());
        }
        catch (NullPointerException |IllegalArgumentException e)
        {
            assertTrue(true);
        }

    }

    private void doTest(String guid, String since, String until, Integer expected)
    {
        final ListMetricsResponse listMetricsResponse = metricApiService.list(guid,since, until);
        assertNotNull(listMetricsResponse);
        assertEquals(MetricsApiStatus.SUCCESS, listMetricsResponse.getStatus());
        final List<MetricEntry>metricEntries = listMetricsResponse.getMetricsEntries();
        assertNotNull(metricEntries);
        final Integer actual = metricEntries.size();
        assertEquals(expected,actual);
        metricEntries.forEach((this::compareAttributes));
    }
    private String getDateFromTimestamp(Long timestamp)
    {
        return dateTimeFormatter.print(new DateTime(timestamp,DateTimeZone.forID(defaultTimeZone)));
    }
    private String insertMetricEntry()
    {
        final RegisterMetricRequest registerMetricRequest = new RegisterMetricRequest();
        registerMetricRequest.setContactEmail("contact@email.com");
        registerMetricRequest.setContactName("Foo Barrio III");
        registerMetricRequest.setName("FooBar Metric");
        registerMetricRequest.setTimezone("UTC");
        final RegisterMetricResponse registerMetricResponse = metricApiService.register(registerMetricRequest);
        assertNotNull(registerMetricResponse);
        assertEquals(MetricsApiStatus.SUCCESS, registerMetricResponse.getStatus());
        final String guid = registerMetricResponse.getGuid();
        assertNotNull(guid);
        return guid;
    }

    private void insertMetricEntries(List<InsertMetricEntryRequest>insertMetricEntryRequests)
    {
        insertMetricEntryRequests.forEach(this::insertMetricEntry);
    }
    private List<InsertMetricEntryRequest> getMetricEntryRequests(String guid)
    {
        final List<InsertMetricEntryRequest>insertMetricEntryRequests = new LinkedList<>();
        twoWeeksOfTimestamps().forEach((timestamp)-> insertMetricEntryRequests.add(getInsertMetricEntryRequest(guid,timestamp)));
        return insertMetricEntryRequests;
    }

    private InsertMetricEntryRequest getInsertMetricEntryRequest(String guid, Long timestamp)
    {
        final InsertMetricEntryRequest insertMetricEntryRequest = new InsertMetricEntryRequest();
        insertMetricEntryRequest.setMetricEntry(new MetricEntry());
        insertMetricEntryRequest.setGuid(guid);
        insertMetricEntryRequest.getMetricEntry().setEntryTimestamp(timestamp);
        insertMetricEntryRequest.getMetricEntry().setAttributes(expectedAttributes);
        return insertMetricEntryRequest;
    }

    private Map<String,Object>getMapping()
    {
        final Map<String,Object>attributes = new HashMap<>();
        attributes.put("boolean",true);
        attributes.put("double",42.0);
        attributes.put("string","42");
        return attributes;
    }
    private void insertMetricEntry(InsertMetricEntryRequest insertMetricEntryRequest)
    {
        final InsertMetricEntryResponse insertMetricEntryResponse = metricApiService.insert(insertMetricEntryRequest);
        assertNotNull(insertMetricEntryResponse);
        assertEquals(MetricsApiStatus.SUCCESS, insertMetricEntryResponse.getStatus());
    }
    private Long getTimestamp(Integer dayOfMonth)
    {
        final Integer previousYear = new DateTime().getYear() - 1;
       return new DateTime(DateTimeZone.forID(defaultTimeZone))
                .withMonthOfYear(8)
                .withYear(previousYear)
               .withDayOfMonth(dayOfMonth)
               .getMillis();
    }
    private List<Long>twoWeeksOfTimestamps()
    {
        final List<Long> timestamps = new LinkedList<>();
        IntStream.rangeClosed(1,upper).forEach((day)->timestamps.add(getTimestamp(day)));
        return timestamps;
    }
    private void compareAttributes(MetricEntry metricEntry)
    {
        final Map<String,Object>actualAttributes = metricEntry.getAttributes();
        assertNotNull(actualAttributes);
        expectedAttributes.forEach((key,value)->validateMapEntry(key,value,actualAttributes));
    }
    private void validateMapEntry(String key, Object value, Map<String,Object> mapping)
    {
        assertTrue(String.format("Key %s not contained in mapping.",key),mapping.containsKey(key));
        assertEquals(String.format("Comparison failed for key '%s'.",key),value,mapping.get(key));
    }
}

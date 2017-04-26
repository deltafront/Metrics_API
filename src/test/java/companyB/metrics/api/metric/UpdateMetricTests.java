package companyB.metrics.api.metric;

import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricResponse;
import companyB.metrics.api.model.Metric;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class UpdateMetricTests extends MetricTestBase
{
    private String guid;
    private UpdateMetricRequest updateMetricRequest;
    private RegisterMetricRequest registerMetricRequest;
    @Before
    public void before()
    {
        registerMetricRequest = registerMetricRequest();
        guid = metricApiService.register(registerMetricRequest).getGuid();
        updateMetricRequest = new UpdateMetricRequest();
        updateMetricRequest.setContactEmail(registerMetricRequest.getContactEmail());
        updateMetricRequest.setContactName(registerMetricRequest.getContactName());
        updateMetricRequest.setTimezone(registerMetricRequest.getTimezone());
        updateMetricRequest.setName(registerMetricRequest.getName());
    }

    @Test
    public void changeName()
    {
        updateMetricRequest.setName("changed");
        doTest(guid,true);
    }
    @Test
    public void changeTimezone()
    {
        updateMetricRequest.setTimezone("changed");
        doTest(guid,true);
    }
    @Test
    public void changeContactName()
    {
        updateMetricRequest.setContactName("changed");
        doTest(guid,true);
    }
    @Test
    public void changeContactEmail()
    {
        updateMetricRequest.setContactEmail("changed@email.com");
        doTest(guid,true);
    }
    @Test
    public void nullName()
    {
        updateMetricRequest.setName(null);
        doTest(guid,false);
    }
    @Test
    public void nullEmail()
    {
        updateMetricRequest.setContactEmail(null);
        doTest(guid,false);
    }
    @Test
    public void nullTimezone()
    {
        updateMetricRequest.setTimezone(null);
        doTest(guid,false);
    }
    @Test
    public void nullContactName()
    {
        updateMetricRequest.setContactName(null);
        doTest(guid,false);
    }
    @Test
    public void emptyStringName()
    {
        updateMetricRequest.setName("");
        doTest(guid,false);
    }
    @Test
    public void emptyStringEmail()
    {
        updateMetricRequest.setContactEmail("");
        doTest(guid,false);
    }
    @Test
    public void invalidEmail()
    {
        updateMetricRequest.setContactEmail("email");
        doTest(guid,false);
    }
    @Test
    public void emptyStringTimezone()
    {
        updateMetricRequest.setTimezone("");
        doTest(guid,false);
    }
    @Test
    public void emptyStringContactName()
    {
        updateMetricRequest.setContactName("");
        doTest(guid,false);
    }
    @Test
    public void nullGuid()
    {
        doTest(null,false);
    }
    @Test
    public void emptyStringGuid()
    {
        doTest("",false);
    }
    @Test
    public void updateToExisting()
    {
        registerMetricRequest.setContactEmail("anotherEmail@you.com");
        final String newGuid = metricApiService.register(registerMetricRequest()).getGuid();
        updateMetricRequest.setContactEmail(registerMetricRequest.getContactEmail());
        final MetricsApiStatus status = metricApiService.update(newGuid, updateMetricRequest).getStatus();
        try
        {
            assertEquals(MetricsApiStatus.FAILURE, status);
        }
        finally
        {
            metricApiService.delete(newGuid);
        }

    }

    private void doTest(String guid, Boolean successExpected)
    {
        final UpdateMetricResponse updateMetricResponse = metricApiService.update(guid,updateMetricRequest);
        assertNotNull(updateMetricResponse);
        final MetricsApiStatus actualStatus = updateMetricResponse.getStatus();
        final MetricsApiStatus expectedStatus = (successExpected) ? MetricsApiStatus.SUCCESS : MetricsApiStatus.FAILURE;
        assertEquals(expectedStatus,actualStatus);
        if(StringUtils.isNotBlank(guid))
        {
            final GetMetricResponse getMetricResponse = metricApiService.get(guid);
            assertNotNull(getMetricResponse);
            final Metric metric = getMetricResponse.getMetric();
            assertNotNull(metric);
            final String expectedName = (successExpected) ? updateMetricRequest.getName() : registerMetricRequest.getName();
            final String expectedContactName = (successExpected) ? updateMetricRequest.getContactName() : registerMetricRequest.getContactName();
            final String expectedContactEmail = (successExpected) ? updateMetricRequest.getContactEmail() : registerMetricRequest.getContactEmail();
            final String expectedTimezone = (successExpected) ? updateMetricRequest.getTimezone() : registerMetricRequest.getTimezone();
            assertEquals(expectedName,metric.getName());
            assertEquals(expectedContactEmail,metric.getContactEmail());
            assertEquals(expectedTimezone,metric.getTimezone());
            assertEquals(expectedContactName,metric.getContactName());
        }

    }
}

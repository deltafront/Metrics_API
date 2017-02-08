package companyB.metrics.api.metric;


import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.model.Metric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetMetricTests extends MetricTestBase
{
    private String guid;
    private RegisterMetricRequest added;

    @Before
    public void before()
    {
        added = registerMetricRequest();
        guid = metricApiService.register(added).getGuid();
    }

    @After
    public void after()
    {
        metricApiService.delete(guid);
    }
    @Test
    public void happyPath()
    {
        doTest(guid, true);
    }
    @Test
    public void nonExistantGuid()
    {
        doTest(guid.toUpperCase(),false);
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
    private void doTest(String guid, Boolean successExpected)
    {
        final GetMetricResponse getMetricResponse = metricApiService.get(guid);
        final MetricsApiStatus status = (successExpected) ? MetricsApiStatus.SUCCESS : MetricsApiStatus.FAILURE;
        assertNotNull(getMetricResponse);
        assertEquals(status, getMetricResponse.getStatus());
        final Metric metric = getMetricResponse.getMetric();
        if(successExpected)
        {
            assertNotNull(metric);
            assertEquals(added.getContactEmail(), metric.getContactEmail());
            assertEquals(added.getContactName(), metric.getContactName());
            assertEquals(added.getName(), metric.getName());
            assertEquals(added.getTimezone(), metric.getTimezone());
            assertEquals(guid, metric.getGuid());
        }
    }
}

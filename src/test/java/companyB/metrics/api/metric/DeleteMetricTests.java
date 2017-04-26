package companyB.metrics.api.metric;

import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DeleteMetricTests extends MetricTestBase
{
    private String guid;
    @Before
    public void before()
    {
        guid = metricApiService.register(registerMetricRequest()).getGuid();
    }
    @Test
    public void happyPath()
    {
        doTest(guid, true);
    }
    @Test
    public void alreadyDeleted()
    {
        doTest(guid,true);
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
    private void doTest(String guid, Boolean successExpected)
    {
        final DeleteMetricResponse deleteMetricResponse = metricApiService.delete(guid);
        final MetricsApiStatus status = (successExpected) ? MetricsApiStatus.SUCCESS : MetricsApiStatus.FAILURE;
        assertNotNull(deleteMetricResponse);
        assertEquals(status, deleteMetricResponse.getStatus());
    }
}

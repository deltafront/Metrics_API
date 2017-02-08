package companyB.metrics.api.metric;

import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;


public class AddMetricTests extends MetricTestBase
{
    private RegisterMetricRequest registerMetricRequest;
    private String guid;

    @Before
    public void before()
    {
       registerMetricRequest = registerMetricRequest();
    }
    @After
    public void after()
    {
        if(null != guid)metricApiService.delete(guid);
    }
    @Test
    public void happyPath()
    {
        doTest(registerMetricRequest,true);
    }
    @Test
    public void nullName()
    {
        registerMetricRequest.setName(null);
        doTest(registerMetricRequest,false);
    }
    @Test
    public void nullTimeZone()
    {
        registerMetricRequest.setTimezone(null);
        doTest(registerMetricRequest,false);
    }
    @Test
    public void emptyName()
    {
        registerMetricRequest.setName("");
        doTest(registerMetricRequest,false);
    }
    @Test
    public void emptyTimeZone()
    {
        registerMetricRequest.setTimezone("");
        doTest(registerMetricRequest,false);
    }
    @Test
    public void nullContactName()
    {
        registerMetricRequest.setContactName(null);
        doTest(registerMetricRequest,false);
    }
    @Test
    public void nullContactEmail()
    {
        registerMetricRequest.setContactEmail(null);
        doTest(registerMetricRequest,false);
    }
    @Test
    public void invalidEmail()
    {
        registerMetricRequest.setContactEmail("email");
        doTest(registerMetricRequest,false);
    }
    @Test
    public void emptyContactName()
    {
        registerMetricRequest.setContactName("");
        doTest(registerMetricRequest,false);
    }
    @Test
    public void emptyContactEmail()
    {
        registerMetricRequest.setContactEmail("");
        doTest(registerMetricRequest,false);
    }
    private void doTest(RegisterMetricRequest registerMetricRequest, Boolean successExpected)
    {
        final RegisterMetricResponse registerMetricResponse = metricApiService.register(registerMetricRequest);
        final MetricsApiStatus expected  = (successExpected) ? MetricsApiStatus.SUCCESS : MetricsApiStatus.FAILURE;
        if(successExpected)assertNotNull(registerMetricResponse.getGuid());
        else assertNull(registerMetricResponse.getGuid());
        assertEquals(expected, registerMetricResponse.getStatus());
        guid = registerMetricResponse.getGuid();
    }
}

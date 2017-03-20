package companyB.metrics.api.metric;

import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ListAndCountMetricsTests extends MetricTestBase
{
    private List<String> guids;

    @Before
    public void before()
    {
        guids = new LinkedList<>();
        IntStream.range(0,100).forEach((index)->
        {
            final RegisterMetricRequest registerMetricRequest = new RegisterMetricRequest();
            registerMetricRequest.setName(String.format("FOO_%s",index));
            registerMetricRequest.setContactEmail("bar@yo.com");
            registerMetricRequest.setTimezone("America/Los_Angeles");
            registerMetricRequest.setContactName("Foo Barrio III");
            final RegisterMetricResponse registerMetricResponse = metricApiService.register(registerMetricRequest);
            assertNotNull(registerMetricResponse);
            assertTrue(registerMetricResponse.getStatus().equals(MetricsApiStatus.SUCCESS));
            guids.add(registerMetricResponse.getGuid());
        });
    }

    @After
    public void after()
    {
        guids.forEach((guid)->
        {
            final DeleteMetricResponse deleteMetricResponse= metricApiService.delete(guid);
            assertNotNull(deleteMetricResponse);
            assertTrue(deleteMetricResponse.getStatus().equals(MetricsApiStatus.SUCCESS));
        });
    }

    @Test
    public void list()
    {
        final Long expected = 100L;
        final List<String> allGuids = metricApiService.listRegisteredMetricGuids();
        assertEquals(expected.intValue(),allGuids.size());
        final Long count = allGuids.stream().filter((guid) -> guids.contains(guid)).count();
        assertEquals(expected,count);
    }

    @Test
    public void count()
    {
        final Integer expected = 100;
        final Integer actual = metricApiService.registeredMetricCount();
        assertEquals(expected,actual);
    }
}

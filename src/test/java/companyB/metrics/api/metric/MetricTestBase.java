package companyB.metrics.api.metric;

import companyB.metrics.api.TestBase;
import companyB.metrics.api.contract.register.RegisterMetricRequest;

public class MetricTestBase extends TestBase
{
    protected RegisterMetricRequest registerMetricRequest()
    {
        final RegisterMetricRequest registerMetricRequest = new RegisterMetricRequest();
        registerMetricRequest.setName("Foo");
        registerMetricRequest.setContactEmail("bar@yo.com");
        registerMetricRequest.setTimezone("America/Los_Angeles");
        registerMetricRequest.setContactName("Foo Barrio III");
        return registerMetricRequest;
    }
}

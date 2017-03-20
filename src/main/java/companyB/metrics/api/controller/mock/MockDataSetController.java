package companyB.metrics.api.controller.mock;

import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.mock.MockDatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/metric/mock")
public class MockDataSetController
{
    @Autowired
    private MockDatasetService mockDatasetService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody RegisterMetricResponse mock()
    {
        return mockDatasetService.mock();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody DeleteMetricResponse clear()
    {
        return mockDatasetService.clear();
    }
}

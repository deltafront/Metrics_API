package companyB.metrics.api.controller;

import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricResponse;
import companyB.metrics.api.service.MetricApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/metric")
public class MetricController
{

    @Autowired
    private MetricApiService metricApiService;

    @ApiOperation(
            value = "register",
            notes = "Registers a Metric. This will fail if more than one metric with the same name is associated with an email address, " +
                    "or if any of the fields are missing, empty or (in the case of the contact email), invalid.",
            consumes = "application/json",
            produces = "application/json")
    @ApiResponses(@ApiResponse(code = 201, response = RegisterMetricResponse.class, message = "Metric inserted? [TRUE/FALSE]."))
    @RequestMapping(method = RequestMethod.POST, value = "")
    public @ResponseBody RegisterMetricResponse register(@RequestBody RegisterMetricRequest registerMetricRequest)
    {
        return metricApiService.register(registerMetricRequest);
    }

    @ApiOperation(
            value = "delete",
            notes = "Deletes a Metric and all of its entries. This will fail if the Metric to be deleted does not exist.",
            produces = "application/json")
    @ApiResponses(@ApiResponse(code = 201, response = DeleteMetricResponse.class, message = "Metric {GUID} deleted? [TRUE/FALSE]."))
    @RequestMapping(method = RequestMethod.DELETE, value = "{guid}")
    public @ResponseBody DeleteMetricResponse delete(@PathVariable String guid)
    {
        return metricApiService.delete(guid);
    }

    @ApiOperation(
            value = "update",
            notes = "Updates a Metric. This will fail if the Metric to be updated does not exist, " +
                    "or it is associated with an email address that already has a metric with that name," +
                    "or if the email field or the timezone field is missing / empty / invalid.",
            consumes = "application/json",
            produces = "application/json")
    @ApiResponses(@ApiResponse(code = 201, response = UpdateMetricResponse.class, message = "Metric {GUID} updated? [TRUE/FALSE]."))
    @RequestMapping(method = RequestMethod.PUT, value = "{guid}")
    public @ResponseBody UpdateMetricResponse update(@PathVariable String guid, @RequestBody UpdateMetricRequest updateMetricRequest)
    {
        return metricApiService.update(guid,updateMetricRequest);
    }

    @ApiOperation(
            value = "get",
            notes = "Gets an existing Metric. This will fail if the Metric to be found does not exist.",
            produces = "application/json")
    @ApiResponses(@ApiResponse(code = 200, response = GetMetricResponse.class, message = "Metric {GUID} found? [TRUE/FALSE]."))
    @RequestMapping(method = RequestMethod.GET, value = "{guid}")
    public @ResponseBody GetMetricResponse get(@PathVariable String guid)
    {
        return metricApiService.get(guid);
    }
}

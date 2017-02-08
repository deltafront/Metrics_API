package companyB.metrics.api.controller;

import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.insert.InsertMetricEntryResponse;
import companyB.metrics.api.contract.list.ListMetricsResponse;
import companyB.metrics.api.service.MetricApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "metrics")
public class MetricsController
{
    @Autowired
    private MetricApiService metricApiService;

    @ApiOperation(
            value = "Inserts a Metric Entry.",
            notes = "Inserts an Entry for a metric. This operation will fail if the GUID is invalid, if the date/time string" +
                    " (if present) is not in 'YYYY-DD-MM'T'HH:mm:ss:SSS' format, or if any of the attribute keys are null / empty string. ",
            produces = "application/json")
    @ApiResponses(@ApiResponse(
            code = 200,
            response = InsertMetricEntryResponse.class,
            message = "Metric Entry inserted for Metric Guid {}? [TRUE/FALSE]"))
    @RequestMapping(method = RequestMethod.POST, value = "insert")
    public @ResponseBody InsertMetricEntryResponse insert(@RequestBody InsertMetricEntryRequest insertMetricEntryRequest)
    {
        return metricApiService.insert(insertMetricEntryRequest);
    }

    @ApiOperation(
            value = "List Metric Entries since date / time",
            notes = "Gets all Metric Entries for a certain GUID since a date / time formatted as such: yyyy-mm-dd'T'HH:MM. " +
                    "This request will fail if the date/time string is improperly formatted.",
            produces = "application/json")
    @ApiResponses(@ApiResponse(
            code = 200,
            response = ListMetricsResponse.class,
            message = "{} Metric Entries found found for Guid {} since {}"))
    @RequestMapping(method = RequestMethod.GET, value = "{guid}/since/{since}")
    public @ResponseBody ListMetricsResponse getSince(@PathVariable String guid, @PathVariable String since)
    {
        return metricApiService.list(guid, since, null);
    }

    @ApiOperation(
            value = "List Metric Entries until date / time",
            notes = "Gets all Metric Entries for a certain GUID from the Unix Epoch (01 January 1970) until a date / time formatted as such: yyyy-mm-dd'T'HH:MM. " +
                    "This request will fail if the date/time string is improperly formatted.",
            produces = "application/json")
    @ApiResponses(@ApiResponse(
            code = 200,
            response = ListMetricsResponse.class,
            message = "{} Metric Entries found found for Guid {} until {}"))
    @RequestMapping(method = RequestMethod.GET, value = "{guid}/until/{until}")
    public @ResponseBody ListMetricsResponse getUntil(@PathVariable String guid, @PathVariable String until)
    {
        return metricApiService.list(guid, null,until);
    }


    @ApiOperation(
            value = "List Metric Entries from  / until date / time",
            notes = "Gets all Metric Entries for a certain GUID from and until a date / time formatted as such: yyyy-mm-dd'T'HH:MM. " +
                    "This request will fail if the date/time string is improperly formatted.",
            produces = "application/json")
    @ApiResponses(@ApiResponse(
            code = 200,
            response = ListMetricsResponse.class,
            message = "{} Metric Entries found found for Guid {} from {} until {}"))
    @RequestMapping(method = RequestMethod.GET, value = "{guid}/since/{since}/until/{until}")
    public @ResponseBody ListMetricsResponse getSinceUntil(@PathVariable String guid, @PathVariable String since, @PathVariable String until)
    {
        return metricApiService.list(guid, since, until);
    }
}

package companyB.metrics.api.utils;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.insert.InsertMetricEntryResponse;
import companyB.metrics.api.contract.list.ListMetricsResponse;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.contract.update.UpdateMetricResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MetricApiResponseUtils
{
    private final Logger LOGGER = LoggerFactory.getLogger(MetricApiResponseUtils.class);
    protected enum RequestType
    {
        Metric,MetricEntry
    }
    protected enum Operation
    {
        Registration,Update,Get,List, Insertion, Deletion
    }
    public GetMetricResponse setMessage(GetMetricResponse getMetricResponse)
    {
       return setMessage(getMetricResponse,String.format("Metric Guid '%s' found? %B",
                getMetricResponse.getGuid(),(null != getMetricResponse.getMetric())),RequestType.Metric,Operation.Get);
    }
    public DeleteMetricResponse setMessage(DeleteMetricResponse deleteMetricResponse)
    {
        return setMessage(deleteMetricResponse,
                String.format("Metric Guid '%s' deleted? %B", deleteMetricResponse.getGuid(),
                        deleteMetricResponse.getStatus().equals(MetricsApiStatus.SUCCESS)),RequestType.Metric,Operation.Deletion);
    }
    public InsertMetricEntryResponse setMessage(InsertMetricEntryResponse insertMetricEntryResponse)
    {
       return setMessage(insertMetricEntryResponse,
                String.format("Metric Entry for Guid '%s' inserted? %B",
                        insertMetricEntryResponse.getGuid(),
                        insertMetricEntryResponse.getStatus().equals(MetricsApiStatus.SUCCESS)),RequestType.MetricEntry,Operation.Insertion);
    }
    public ListMetricsResponse setMessage(ListMetricsResponse listMetricsResponse)
    {
        return setMessage(listMetricsResponse,
                String.format("Found %d entries for Metric Guid '%s' (since %s until %s)",
                        listMetricsResponse.getMetricsEntries().size(),
                        listMetricsResponse.getGuid(),
                        listMetricsResponse.getSince(),
                        listMetricsResponse.getUntil()),RequestType.MetricEntry,Operation.List);
    }
    public RegisterMetricResponse setMessage(RegisterMetricResponse registerMetricResponse)
    {
        return setMessage(registerMetricResponse,
                String.format("Metric name '%s' registered? %B",
                        registerMetricResponse.getName(),
                        registerMetricResponse.getStatus().equals(MetricsApiStatus.SUCCESS)),RequestType.Metric,Operation.Registration);
    }
    public UpdateMetricResponse setMessage(UpdateMetricResponse updateMetricResponse)
    {
        return setMessage(updateMetricResponse,
                String.format("Metric Guid '%s' updated? %B",
                        updateMetricResponse.getGuid(),
                        updateMetricResponse.getStatus().equals(MetricsApiStatus.SUCCESS)),RequestType.Metric,Operation.Update);
    }

    public <T extends BaseMetricsResponse> void setResponseStatus(T baseMetricsResponse, Boolean condition)
    {
        baseMetricsResponse.setStatus( condition ? MetricsApiStatus.SUCCESS : MetricsApiStatus.FAILURE);
    }
    private <T extends BaseMetricsResponse> T setMessage(T baseMetricsResponse, String message, RequestType requestType, Operation operation)
    {
        final String fromResponse = baseMetricsResponse.getMessage();
        baseMetricsResponse.setMessage(String.format("%s%s",message, (null == fromResponse) ? "" : String.format(":=>%s",fromResponse)));
        logDebug(baseMetricsResponse,requestType,operation);
        return baseMetricsResponse;
    }
    private void logDebug(BaseMetricsResponse response, MetricApiResponseUtils.RequestType requestType, MetricApiResponseUtils.Operation operation)
    {
        LOGGER.debug("{} Guid '{}' {} Status: '{}' Message: '{}'",requestType,response.getGuid(),operation, response.getStatus(),response.getMessage());
    }
}

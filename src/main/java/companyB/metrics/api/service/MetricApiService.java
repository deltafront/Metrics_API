package companyB.metrics.api.service;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.contract.MetricsApiStatus;
import companyB.metrics.api.contract.delete.DeleteMetricResponse;
import companyB.metrics.api.contract.get.GetMetricResponse;
import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.insert.InsertMetricEntryResponse;
import companyB.metrics.api.contract.list.ListMetricsResponse;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.register.RegisterMetricResponse;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricResponse;
import companyB.metrics.api.data_access.MetricDao;
import companyB.metrics.api.data_access.MetricsDao;
import companyB.metrics.api.utils.DateUtils;
import companyB.metrics.api.utils.MetricApiResponseUtils;
import companyB.metrics.api.utils.SqlUtils;
import companyB.metrics.api.utils.ValidationUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricApiService
{
    @Autowired
    private MetricDao metricDao;
    @Autowired
    private MetricsDao metricsDao;
    @Autowired
    private MetricApiResponseUtils metricApiResponseUtils;
    @Autowired
    private ValidationUtils validationUtils;
    @Autowired
    private SqlUtils sqlUtils;
    @Autowired
    private DateUtils dateUtils;

    private final Logger LOGGER = LoggerFactory.getLogger(MetricApiService.class);

    public GetMetricResponse get(String guid)
    {
        GetMetricResponse getMetricResponse = new GetMetricResponse();
        try
        {
            Validate.notBlank(guid, "Metric Guid must be provided.");
            getMetricResponse.setGuid(guid);
            getMetricResponse = metricDao.get(guid);
        }
        catch (Exception e)
        {
            handleException(e,getMetricResponse);
        }
        getMetricResponse.setGuid(guid);
        return metricApiResponseUtils.setMessage(getMetricResponse);
    }
    public RegisterMetricResponse register(RegisterMetricRequest registerMetricRequest)
    {
        RegisterMetricResponse registerMetricResponse = new RegisterMetricResponse();
        try
        {
            registerMetricRequest.validate();
            final String email = registerMetricRequest.getContactEmail();
            Validate.isTrue(validationUtils.validateEmail(email),String.format("Email '%s' is not valid.",email));
            registerMetricResponse = metricDao.register(registerMetricRequest);
        }
        catch (Exception e)
        {
            handleException(e,registerMetricResponse);
        }
        return metricApiResponseUtils.setMessage(registerMetricResponse);
    }
    public UpdateMetricResponse update(String guid, UpdateMetricRequest updateMetricRequest)
    {
        UpdateMetricResponse updateMetricResponse = new UpdateMetricResponse();
        try
        {
            Validate.notBlank(guid, "Metric Guid must be provided.");
            updateMetricRequest.validate();
            final String email = updateMetricRequest.getContactEmail();
            Validate.isTrue(validationUtils.validateEmail(email),String.format("Email '%s' is not valid.",email));
            updateMetricResponse.setGuid(guid);
            updateMetricResponse.setName(updateMetricRequest.getName());
            updateMetricResponse = metricDao.update(guid,updateMetricRequest);
        }
        catch (Exception e)
        {
            handleException(e,updateMetricResponse);
        }
        return metricApiResponseUtils.setMessage(updateMetricResponse);
    }
    public DeleteMetricResponse delete(String guid)
    {
        DeleteMetricResponse deleteMetricResponse = new DeleteMetricResponse();
        try
        {
            Validate.notBlank(guid, "Metric Guid must be provided.");
            deleteMetricResponse.setGuid(guid);
            deleteMetricResponse = metricDao.delete(guid);
        }
        catch (Exception e)
        {
            handleException(e,deleteMetricResponse);
        }

        return metricApiResponseUtils.setMessage(deleteMetricResponse);
    }
    public ListMetricsResponse list(String guid, String since, String until)
    {
        final Long sinceTimestamp = (null == since) ? 0L : dateUtils.toTimestamp(since);
        final Long untilTimestamp = dateUtils.toTimestamp(until);
        final String sinceString = dateUtils.fromTimestamp(sinceTimestamp);
        final String untilString = dateUtils.fromTimestamp(untilTimestamp);
        ListMetricsResponse listMetricsResponse = new ListMetricsResponse();
        listMetricsResponse.setSince(sinceString);
        listMetricsResponse.setUntil(untilString);
        listMetricsResponse.setGuid(guid);
        try
        {
            listMetricsResponse = metricsDao.list(guid,sinceString,untilString);
        }
        catch (Exception e)
        {
            handleException(e,listMetricsResponse);
        }
        return metricApiResponseUtils.setMessage(listMetricsResponse);
    }
    public InsertMetricEntryResponse insert(InsertMetricEntryRequest insertMetricEntryRequest)
    {
        InsertMetricEntryResponse insertMetricEntryResponse = new InsertMetricEntryResponse();
        try
        {
            insertMetricEntryRequest.validate();
            final String entryDate = insertMetricEntryRequest.getMetricEntry().getEntryDate();
            Validate.isTrue(validationUtils.validateDateString(entryDate),"Date Entered (%s) is not in 'YYYY-MM-DD'T'HH:mm:ss:SSS' format.");
            final String guid = insertMetricEntryRequest.getGuid();
            insertMetricEntryResponse.setGuid(guid);
            insertMetricEntryResponse = metricsDao.insert(insertMetricEntryRequest);
        }
        catch (Exception e)
        {
            handleException(e,insertMetricEntryResponse);
        }
        return metricApiResponseUtils.setMessage(insertMetricEntryResponse);
    }

    public List<String> listRegisteredMetricGuids()
    {
        final List<String>guids = new LinkedList<>();
        try
        {
            guids.addAll(metricDao.list());
        }
        catch (SQLException e)
        {
            handleException(e,null);
        }
        return guids;
    }

    public Integer registeredMetricCount()
    {
        final AtomicInteger count = new AtomicInteger(0);
        try
        {
            count.set(metricDao.getCount());
        }
        catch (SQLException e)
        {
            handleException(e,null);
        }
        return count.get();
    }

    private void handleException(Exception e, BaseMetricsResponse response)
    {
        final String message = sqlUtils.handleException(e);
        if(null != response)
        {
            response.setStatus(MetricsApiStatus.FAILURE);
            response.setMessage(message);
        }
        LOGGER.error(message);
    }
}

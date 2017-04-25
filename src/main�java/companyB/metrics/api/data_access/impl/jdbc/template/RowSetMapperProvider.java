package companyB.metrics.api.data_access.impl.jdbc.template;

import com.google.gson.Gson;
import companyB.metrics.api.model.Metric;
import companyB.metrics.api.model.MetricEntry;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RowSetMapperProvider
{
    RowMapper<Metric>metricRowMapper = (resultSet,num)->{
        final Metric metric = new Metric();
        metric.setContactEmail(resultSet.getString("CONTACT_EMAIL"));
        metric.setContactName(resultSet.getString("CONTACT_NAME"));
        metric.setName(resultSet.getString("NAME"));
        metric.setTimezone(resultSet.getString("TIMEZONE"));
        metric.setGuid(resultSet.getString("GUID"));
        return metric;
    };

    RowMapper<MetricEntry>metricEntryRowMapper = (resultSet, i) -> {
        final MetricEntry metricEntry = new MetricEntry();
        final String attributes = resultSet.getString("ATTRIBUTES");
        Map<String,Object> attributesMapping = new HashMap<>();
        attributesMapping = new Gson().fromJson(attributes,attributesMapping.getClass());
        metricEntry.setAttributes(attributesMapping);
        metricEntry.setEntryTimestamp(resultSet.getLong("ENTRY_TIMESTAMP"));
        metricEntry.setEntryDate(resultSet.getString("ENTRY_DATE"));
        metricEntry.setId(resultSet.getLong("ID"));
        return metricEntry;
    };
}

package companyB.metrics.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DateUtils implements InitializingBean
{
    private DateTimeFormatter dateTimeFormatter;
    private DateTime now;
    @Value("${default.datetime.format}")
    private String dateTimeFormat;
    @Value("${default.datetime.timezone}")
    private String defaultTimeZone;

    public Long toTimestamp(String dateString)
    {
        return (StringUtils.isNotBlank(dateString)) ?
                dateTimeFormatter.parseDateTime(dateString.replace(".",":")).getMillis():
                now.getMillis();
    }
    public String fromTimestamp(Long timestamp)
    {
        final DateTime dateTime = (null == timestamp) ?
                now :
                new DateTime(timestamp, DateTimeZone.forID(defaultTimeZone));
        return dateTimeFormatter.print(dateTime);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);
        now = new DateTime(DateTimeZone.forID(defaultTimeZone));
    }
}

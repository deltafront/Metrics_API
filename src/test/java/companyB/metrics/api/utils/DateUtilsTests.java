package companyB.metrics.api.utils;

import companyB.metrics.api.TestBase;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DateUtilsTests extends TestBase
{
    private Long expectedTimestamp;
    private String expectedDateString;

    @Before
    public void before()
    {
        final DateTime dateTime = new DateTime()
                .withYear(1969)
                .withMonthOfYear(8)
                .withDayOfMonth(3)
                .withHourOfDay(15)
                .withMinuteOfHour(15)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        expectedTimestamp = dateTime.getMillis();
        expectedDateString = "1969-08-03T15:15:00:0";
    }

    @Test
    public void fromTimestamp()
    {
        assertEquals(expectedDateString, dateUtils.fromTimestamp(expectedTimestamp));
    }

    @Test
    public void toTimestamp()
    {
        assertEquals(expectedTimestamp,dateUtils.toTimestamp(expectedDateString));
    }
}

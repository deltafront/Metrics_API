package companyB.metrics.api;


import companyB.metrics.api.service.MetricApiService;
import companyB.metrics.api.utils.DateUtils;
import companyB.metrics.api.utils.JdbcSqlConnection;
import companyB.metrics.api.utils.SqlUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestBase
{
    @Autowired
    protected MetricApiService metricApiService;
    @Autowired
    protected DateUtils dateUtils;
    @Autowired
    protected JdbcSqlConnection jdbcSqlConnection;
    @Autowired
    protected SqlUtils sqlUtils;
}


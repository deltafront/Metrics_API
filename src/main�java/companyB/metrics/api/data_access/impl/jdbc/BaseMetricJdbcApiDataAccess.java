package companyB.metrics.api.data_access.impl.jdbc;


import companyB.metrics.api.utils.JdbcSqlConnection;
import companyB.metrics.api.utils.SqlUtils;
import companyB.metrics.api.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseMetricJdbcApiDataAccess
{
    final Logger LOGGER = LoggerFactory.getLogger(BaseMetricJdbcApiDataAccess.class);
    @Autowired
    protected JdbcSqlConnection jdbcSqlConnection;
    @Autowired
    protected SqlUtils sqlUtils;
    @Autowired
    protected ValidationUtils validationUtils;
}

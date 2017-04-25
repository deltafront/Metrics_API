package companyB.metrics.api.data_access.impl.jdbc.template;

import companyB.metrics.api.utils.SqlGenerator;
import companyB.metrics.api.utils.SqlUtils;
import companyB.metrics.api.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class BaseMetricJdbcTemplateApiDataAccess
{
    final Logger LOGGER = LoggerFactory.getLogger(BaseMetricJdbcTemplateApiDataAccess.class);

    @Autowired
    protected SqlUtils sqlUtils;
    @Autowired
    protected ValidationUtils validationUtils;
    @Autowired
    protected SqlGenerator sqlGenerator;
    @Autowired
    protected RowSetMapperProvider rowSetMapperProvider;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    Integer insertUpdateDelete(String sql) throws SQLException
    {
        return this.jdbcTemplate.update(sql);
    }
}

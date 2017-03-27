package companyB.metrics.api.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlywayService
{
    @Value("${jdbc.username}")
    private  String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.driverManager}")
    private String driverClass;
    @Value("${flyway.locations}")
    private String flywayDbScriptsLocations;
    private final Logger LOGGER = LoggerFactory.getLogger(FlywayService.class);

    public void migrate()
    {
        final BasicDataSource dataSource = getBasicDataSource();
        final Flyway flyway = getFlyway(dataSource);
        LOGGER.info(String.format("Applied database migrations from '%s', result: %s.",
                flywayDbScriptsLocations,flyway.migrate()));
    }

    private Flyway getFlyway(BasicDataSource dataSource)
    {
        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(flywayDbScriptsLocations);
        return flyway;
    }

    private BasicDataSource getBasicDataSource()
    {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        return dataSource;
    }

}

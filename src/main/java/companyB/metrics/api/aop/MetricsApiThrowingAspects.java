package companyB.metrics.api.aop;

import companyB.metrics.api.data_access.MetricDao;
import companyB.metrics.api.data_access.MetricsDao;
import companyB.metrics.api.utils.SqlUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Aspect
@Component
public class MetricsApiThrowingAspects
{
    private final Logger metricDaoLogger = LoggerFactory.getLogger(MetricDao.class);
    private final Logger metricsDaoLogger = LoggerFactory.getLogger(MetricsDao.class);
    @Autowired
    private CounterService counterService;

    @Autowired
    private SqlUtils sqlUtils;

    @AfterThrowing(pointcut="execution(* companyB.metrics.api.data_access.MetricDao.*(..))",
            throwing="throwable")
    public void metricDao(JoinPoint joinPoint, SQLException throwable)
    {
        log(joinPoint,throwable,metricDaoLogger);
    }

    @AfterThrowing(pointcut="execution(* companyB.metrics.api.data_access.MetricsDao.*(..))",
            throwing="throwable")
    public void metricsDao(JoinPoint joinPoint, SQLException throwable)
    {
        log(joinPoint,throwable,metricsDaoLogger);
    }

    private void log(JoinPoint joinPoint, SQLException e, Logger logger)
    {
        logger.error(sqlUtils.handleSqlException(e),e);
        counterService.increment(String.format("counter.%s.errors",joinPoint.getSignature().getName()));
    }

}

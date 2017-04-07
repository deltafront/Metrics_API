package companyB.metrics.api.aop;

import companyB.metrics.api.utils.SqlUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Aspect
@Component
public class MetricServiceThrowingAspects
{
    @Autowired
    private CounterService counterService;

    @Autowired
    private SqlUtils sqlUtils;

    @AfterThrowing(pointcut="execution(* companyB.metrics.api.data_access.impl.jdbc.MetricDaoJdbcImpl.*(..))",
            throwing="throwable")
    public void metricDao(JoinPoint joinPoint, SQLException throwable)
    {
        log(joinPoint,throwable);
    }

    @AfterThrowing(pointcut="execution(* companyB.metrics.api.data_access.impl.jdbc.MetricsDaoJdbcImpl.*(..))",
            throwing="throwable")
    public void metricsDao(JoinPoint joinPoint, SQLException throwable)
    {
        log(joinPoint,throwable);
    }

    private void log(JoinPoint joinPoint, SQLException e)
    {
        sqlUtils.handleSqlException(e);
        counterService.increment(String.format("counter.%s.errors",joinPoint.toShortString()));
    }

}

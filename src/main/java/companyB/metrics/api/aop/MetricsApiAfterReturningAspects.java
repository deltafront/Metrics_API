package companyB.metrics.api.aop;

import companyB.metrics.api.contract.BaseMetricsResponse;
import companyB.metrics.api.service.MetricApiService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricsApiAfterReturningAspects
{
    private final Logger LOGGER = LoggerFactory.getLogger(MetricApiService.class);

    @Autowired
    private CounterService counterService;

    @AfterReturning(pointcut = "execution(* companyB.metrics.api.service.MetricApiService.*(..))",
            returning= "response")
    public void afterReturning(JoinPoint joinPoint, Object response)
    {
        if(response instanceof BaseMetricsResponse)
        {
            final BaseMetricsResponse baseMetricsResponse = (BaseMetricsResponse)response;
            LOGGER.info("Operation={} GUID={} Status={} Message={} ",joinPoint.getSignature().getName(),baseMetricsResponse.getGuid(),
                    baseMetricsResponse.getStatus(),baseMetricsResponse.getMessage());
            final String counterString = String.format("counter.%s.%s.%s",joinPoint.getSignature().getName(),baseMetricsResponse.getGuid(), baseMetricsResponse.getStatus());
            counterService.increment(counterString);
        }
    }


}

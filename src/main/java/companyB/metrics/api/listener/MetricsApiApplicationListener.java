package companyB.metrics.api.listener;

import companyB.metrics.api.service.FlywayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MetricsApiApplicationListener implements ApplicationListener<ApplicationReadyEvent>
{
    @Autowired
    private FlywayService flywayService;
    private final Logger LOGGER = LoggerFactory.getLogger(MetricsApiApplicationListener.class);
    private final List<String>tasks = Arrays.asList("Performing database migrations.");

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent)
    {
        LOGGER.info("Starting the following tasks upon startup:");
        tasks.forEach(LOGGER::info);
        flywayService.migrate();
    }

}

package br.eti.archanjo.webcron.quartz.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JobListenerImpl implements JobListener {
    private static final Logger logger = LoggerFactory.getLogger(JobListenerImpl.class);

    private final String name = "MainListener";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.debug(String.format("Jobs started %s", context.getJobDetail().getKey().toString()));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.info(context.toString());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.debug(String.format("Jobs executed %s", context.getJobDetail().getKey().toString()));
    }
}

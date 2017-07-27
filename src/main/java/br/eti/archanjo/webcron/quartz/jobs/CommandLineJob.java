package br.eti.archanjo.webcron.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommandLineJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(CommandLineJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("running");
    }
}

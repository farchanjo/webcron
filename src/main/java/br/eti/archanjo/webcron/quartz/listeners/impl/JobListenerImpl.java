package br.eti.archanjo.webcron.quartz.listeners.impl;

import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.entities.mongo.ExecutionStatusEntity;
import br.eti.archanjo.webcron.repositories.mongo.ExecutionStatusRepository;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
public class JobListenerImpl implements JobListener {
    private final ExecutionStatusRepository executionStatusRepository;
    private static final Logger logger = LoggerFactory.getLogger(JobListenerImpl.class);
    private JobsDTO job;
    private final String name = "MainListener";

    @Autowired
    public JobListenerImpl(ExecutionStatusRepository executionStatusRepository) {
        this.executionStatusRepository = executionStatusRepository;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.info(context.toString());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        try {
            feedJob(context);
            save(context, jobException);
        } catch (IOException e) {
            logger.error("JobListenerImpl{jobWasExecuted}", e);
        }
        logger.debug(String.format("Jobs executed %s", context.getJobDetail().getKey().toString()));
    }

    /**
     * @param context      {@link JobExecutionContext}
     * @param jobException {@link JobExecutionException}
     */
    private void save(JobExecutionContext context, JobExecutionException jobException) throws IOException {
        Path output = (Path) context.getResult();
        ExecutionStatusEntity.ExecutionStatusEntityBuilder builder = ExecutionStatusEntity.builder();
        builder.created(new Date());
        builder.nextFireTime(context.getNextFireTime());
        builder.fireTime(context.getFireTime());
        builder.jobRunTime(context.getJobRunTime());
        builder.prevFireTime(context.getPreviousFireTime());
        builder.scheduledFireTime(context.getScheduledFireTime());
        if (output != null) {
            builder.output(FileUtils.readFileToString(output.toFile()));
            if (output.toFile().delete())
                logger.debug(String.format("%s file deleted", output.toAbsolutePath()));
        } else {
            builder.output("No output");
        }
        if (jobException != null) {
            builder.errors(true);
            builder.errorMessage(jobException.getMessage());
        }
        builder.job(getJob());
        executionStatusRepository.save(builder.build());
    }

    /**
     * @param context {@link JobExecutionContext}
     */
    private void feedJob(JobExecutionContext context) {
        job = (JobsDTO) context.getMergedJobDataMap().get("data");
    }
}

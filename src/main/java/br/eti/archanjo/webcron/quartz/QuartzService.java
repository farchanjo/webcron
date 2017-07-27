package br.eti.archanjo.webcron.quartz;

import br.eti.archanjo.webcron.constants.QuartzContants;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.enums.AsyncType;
import br.eti.archanjo.webcron.exceptions.BadRequestException;
import br.eti.archanjo.webcron.quartz.jobs.CommandLineJob;
import br.eti.archanjo.webcron.quartz.listeners.impl.JobListenerImpl;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuartzService {
    private static final Logger logger = LoggerFactory.getLogger(QuartzService.class);
    private final Scheduler scheduler;

    @Autowired
    public QuartzService(Scheduler scheduler, JobListenerImpl jobListener) {
        this.scheduler = scheduler;
        try {
            scheduler.getListenerManager().addJobListener(jobListener);
        } catch (SchedulerException e) {
            logger.warn("QuartzService", e);
        }
    }

    /**
     * @param job {@link JobsDTO}
     */
    public void saveJob(JobsDTO job) throws BadRequestException, SchedulerException {
        checkIfAlreadyRegistered(job);
        switch (job.getAsync()) {
            case PERIODIC:
                Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(getTriggerFormat(job), QuartzContants.THREAD_GROUP_PERIODIC));
                selectStatus(job, trigger);
                break;
            case CRON:
                if (!CronSequenceGenerator.isValidExpression(job.getCron())) {
                    throw new BadRequestException(String.format("%s is not valid", job.getCron()));
                }
                trigger = scheduler.getTrigger(TriggerKey.triggerKey(getTriggerFormat(job), QuartzContants.THREAD_GROUP_CRON));
                selectStatus(job, trigger);
                break;
        }
    }

    /**
     * @param job {@link JobsDTO}
     */
    public void deleteJob(JobsDTO job) throws SchedulerException {
        switch (job.getAsync()) {
            case PERIODIC:
                scheduler.deleteJob(JobKey.jobKey(getJobsFormat(job), QuartzContants.THREAD_GROUP_PERIODIC));
                break;
            case CRON:
                scheduler.deleteJob(JobKey.jobKey(getJobsFormat(job), QuartzContants.THREAD_GROUP_CRON));
                break;
        }
    }

    /**
     * @param job     {@link JobsDTO}
     * @param trigger {@link Trigger}
     */
    private void selectStatus(JobsDTO job, Trigger trigger) throws SchedulerException {
        switch (job.getStatus()) {
            case ENABLE:
                if (trigger == null) {
                    scheduler.scheduleJob(getJobsDetail(job), getTrigger(job));
                } else {
                    scheduler.rescheduleJob(trigger.getKey(), getTrigger(job));
                }
                break;
            case DISABLE:
                if (trigger != null)
                    stopTrigger(trigger);
                break;
        }
    }

    /**
     * @param trigger {@link Trigger}
     */
    private void stopTrigger(Trigger trigger) throws SchedulerException {
        scheduler.unscheduleJob(trigger.getKey());
    }

    /**
     * @param job {@link JobsDTO}
     * @return {@link JobDetail}
     */
    private JobDetail getJobsDetail(JobsDTO job) {
        JobBuilder builder = JobBuilder.newJob(CommandLineJob.class);
        switch (job.getAsync()) {
            case CRON:
                builder.withIdentity(getJobsFormat(job), QuartzContants.THREAD_GROUP_CRON);
                break;
            case PERIODIC:
                builder.withIdentity(getJobsFormat(job), QuartzContants.THREAD_GROUP_PERIODIC);
                break;
        }
        builder.setJobData(getJobDataMap(job));
        return builder.build();
    }

    /**
     * @param job {@link JobsDTO}
     * @return {@link Trigger}
     */
    private Trigger getTrigger(JobsDTO job) throws SchedulerException {
        TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger();
        switch (job.getAsync()) {
            case CRON:
                builder.withIdentity(getTriggerFormat(job), QuartzContants.THREAD_GROUP_CRON);
                builder.withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()));
                break;
            case PERIODIC:
                builder.withIdentity(getTriggerFormat(job), QuartzContants.THREAD_GROUP_PERIODIC);
                builder.withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMilliseconds(TimeUnit.MILLISECONDS.convert(job.getFixedRate(), job.getUnit()))
                        .repeatForever());
                break;
        }
        return builder.build();

    }

    /**
     * @param jobs {@link JobsDTO}
     */
    private void checkIfAlreadyRegistered(JobsDTO jobs) throws SchedulerException {
        scheduler.getTriggerGroupNames()
                .forEach(groupName -> {
                    try {
                        scheduler.getTriggerKeys(GroupMatcher.groupEquals(groupName))
                                .parallelStream()
                                .forEach(trigger -> {
                                    switch (jobs.getAsync()) {
                                        case CRON:
                                            try {
                                                if (trigger.getName().contains(String.format("%s-%s", jobs.getId(), AsyncType.PERIODIC).toLowerCase())) {
                                                    deleteJob(JobsDTO.builder()
                                                            .id(jobs.getId())
                                                            .name(jobs.getName())
                                                            .async(AsyncType.PERIODIC)
                                                            .build());
                                                }
                                            } catch (SchedulerException e) {
                                                logger.warn("QuartzService{checkIfAlreadyRegistered}", e);
                                            }
                                            break;
                                        case PERIODIC:
                                            try {
                                                if (trigger.getName().contains(String.format("%s-%s", jobs.getId(), AsyncType.CRON).toLowerCase())) {
                                                    deleteJob(JobsDTO.builder()
                                                            .id(jobs.getId())
                                                            .name(jobs.getName())
                                                            .async(AsyncType.CRON)
                                                            .build());
                                                }
                                            } catch (SchedulerException e) {
                                                logger.warn("QuartzService{checkIfAlreadyRegistered}", e);
                                            }
                                            break;
                                    }
                                });
                    } catch (SchedulerException e) {
                        logger.warn("QuartzService{checkIfAlreadyRegistered}", e);
                    }
                });
    }

    /**
     * @param job {@link JobsDTO}
     * @return {@link String}
     */
    private String getTriggerFormat(JobsDTO job) {
        return String.format("%s-%s-Trigger", job.getId(), job.getAsync()).toLowerCase();
    }

    /**
     * @param job {@link JobsDTO}
     * @return {@link String}
     */
    private String getJobsFormat(JobsDTO job) {
        return String.format("%s-%s-Job", job.getId(), job.getAsync()).toLowerCase();
    }

    /**
     * @param job {@link JobsDTO}
     * @return {@link JobDataMap}
     */
    private JobDataMap getJobDataMap(JobsDTO job) {
        JobDataMap map = new JobDataMap();
        map.put("data", job);
        return map;
    }

}

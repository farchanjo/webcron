package br.eti.archanjo.webcron.quartz;

import br.eti.archanjo.webcron.constants.QuartzContants;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.exceptions.BadRequestException;
import br.eti.archanjo.webcron.quartz.jobs.WebJobs;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuartzService {
    private final Scheduler scheduler;

    @Autowired
    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * @param job {@link JobsDTO}
     */
    public void saveJob(JobsDTO job) throws BadRequestException, SchedulerException {
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
        JobBuilder builder = JobBuilder.newJob(WebJobs.class);
        switch (job.getAsync()) {
            case CRON:
                builder.withIdentity(getJobsFormat(job), QuartzContants.THREAD_GROUP_CRON);
                break;
            case PERIODIC:
                builder.withIdentity(getJobsFormat(job), QuartzContants.THREAD_GROUP_PERIODIC);
                break;
        }
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
                        .withIntervalInSeconds((int) job.getUnit().convert(job.getFixedRate().longValue(), job.getUnit()))
                        .repeatForever());
                break;
        }
        return builder.build();

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

}

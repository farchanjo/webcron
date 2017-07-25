package br.eti.archanjo.webcron.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@EnableAsync
public class Schedulers implements SchedulingConfigurer {
    private final Logger logger = LoggerFactory.getLogger(Schedulers.class);

    private final ThreadPoolTaskScheduler taskSchedulerExecutor;

    @Autowired
    public Schedulers(ThreadPoolTaskScheduler taskSchedulerExecutor) {
        this.taskSchedulerExecutor = taskSchedulerExecutor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskSchedulerExecutor);
    }
}

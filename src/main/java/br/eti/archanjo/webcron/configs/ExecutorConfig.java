package br.eti.archanjo.webcron.configs;

import br.eti.archanjo.webcron.threads.rejects.RejectExecutionImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ExecutorConfig {
    @Bean(name = "taskSchedulerExecutor")
    public ThreadPoolTaskScheduler taskSchedulerExecutor() {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
        threadPool.setPoolSize(80);
        threadPool.setRejectedExecutionHandler(new RejectExecutionImpl());
        threadPool.setThreadNamePrefix("Scheduler-");
        threadPool.setThreadGroupName("Scheduler");
        return threadPool;
    }
}
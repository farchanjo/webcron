package br.eti.archanjo.webcron.configs;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzConfig {
    private final Logger logger = LoggerFactory.getLogger(QuartzConfig.class);

    @Bean
    public Scheduler scheduler() {
        try {
            SchedulerFactory sf = new StdSchedulerFactory(quartzProperties());
            Scheduler scheduler = sf.getScheduler();
            scheduler.start();
            return scheduler;
        } catch (SchedulerException | IOException e) {
            logger.warn("QuartzConfig{scheduler}", e);
        }
        return null;
    }
    
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}

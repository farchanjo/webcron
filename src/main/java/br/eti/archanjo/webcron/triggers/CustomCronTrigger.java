package br.eti.archanjo.webcron.triggers;

import br.eti.archanjo.webcron.dtos.JobsDTO;
import lombok.Getter;
import org.springframework.scheduling.support.CronTrigger;

import java.util.TimeZone;

@Getter
public class CustomCronTrigger extends CronTrigger {
    private final JobsDTO job;

    public CustomCronTrigger(String expression, JobsDTO job) {
        super(expression);
        this.job = job;
    }

    public CustomCronTrigger(String expression, TimeZone timeZone, JobsDTO job) {
        super(expression, timeZone);
        this.job = job;
    }
}

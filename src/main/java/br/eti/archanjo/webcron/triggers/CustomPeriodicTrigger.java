package br.eti.archanjo.webcron.triggers;

import br.eti.archanjo.webcron.dtos.JobsDTO;
import lombok.Getter;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

@Getter
public class CustomPeriodicTrigger extends PeriodicTrigger {
    private final JobsDTO job;

    public CustomPeriodicTrigger(long period, JobsDTO job) {
        super(period);
        this.job = job;
    }

    public CustomPeriodicTrigger(long period, TimeUnit timeUnit, JobsDTO job) {
        super(period, timeUnit);
        this.job = job;
    }
}

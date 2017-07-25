package br.eti.archanjo.webcron.facade;

import br.eti.archanjo.webcron.domain.Cron;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CronFacade {
    private final Cron cron;

    @Autowired
    public CronFacade(Cron cron) {
        this.cron = cron;
    }

    public List<JobsDTO> listJobs(UserDTO client, Integer limit, Integer page) {
        return cron.listJobs(client, limit, page);
    }
}

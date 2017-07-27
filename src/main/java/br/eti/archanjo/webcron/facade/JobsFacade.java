package br.eti.archanjo.webcron.facade;

import br.eti.archanjo.webcron.domain.Jobs;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JobsFacade {
    private final Jobs jobs;

    @Autowired
    public JobsFacade(Jobs jobs) {
        this.jobs = jobs;
    }

    public Page<JobsDTO> listAll(UserDTO client, Integer limit, Integer page) {
        return jobs.listAll(client, limit, page);
    }

    public JobsDTO save(UserDTO client, JobsDTO job) {
        return jobs.save(client, job);
    }
}

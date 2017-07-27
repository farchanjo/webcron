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

    /**
     * @param client {@link UserDTO}
     * @param limit  {@link Integer}
     * @param page   {@link Integer}
     * @return {@link Page<JobsDTO>}
     */
    public Page<JobsDTO> listAll(UserDTO client, Integer limit, Integer page) {
        return jobs.listAll(client, limit, page);
    }

    /**
     * @param client {@link UserDTO}
     * @param job    {@link JobsDTO }
     * @return {@link JobsDTO}
     */
    public JobsDTO save(UserDTO client, JobsDTO job) {
        return jobs.save(client, job);
    }
}

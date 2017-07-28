package br.eti.archanjo.webcron.facade;

import br.eti.archanjo.webcron.domain.Jobs;
import br.eti.archanjo.webcron.dtos.ExecutionStatusDTO;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.exceptions.NotFoundException;
import org.quartz.SchedulerException;
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
    public Page<JobsDTO> listAll(UserDTO client, Integer limit, Integer page) throws Exception {
        return jobs.listAll(client, limit, page);
    }

    /**
     * @param client {@link UserDTO}
     * @param job    {@link JobsDTO }
     * @return {@link JobsDTO}
     */
    public JobsDTO save(UserDTO client, JobsDTO job) throws Exception {
        if (job.getCommand() == null)
            throw new NotFoundException("Missing commmand to run");
        return jobs.save(client, job);
    }

    /**
     * @param client {@link UserDTO}
     * @param id     {@link Long}
     * @return {@link Boolean}
     */
    public boolean delete(UserDTO client, Long id) throws SchedulerException {
        return jobs.delete(client, id);
    }

    /**
     * @param client {@link UserDTO}
     * @param limit {@link Integer}
     * @param page {@link Integer}
     * @return {@link Page<ExecutionStatusDTO>}
     */
    public Page<ExecutionStatusDTO> listResults(UserDTO client, Integer limit, Integer page) {
        return jobs.listResults(client, limit, page);
    }
}

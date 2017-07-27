package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.repositories.mysql.JobsRepository;
import br.eti.archanjo.webcron.utils.parsers.JobsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Jobs {
    private final JobsRepository jobsRepository;

    @Autowired
    public Jobs(JobsRepository jobsRepository) {
        this.jobsRepository = jobsRepository;
    }

    public Page<JobsDTO> listAll(UserDTO client, Integer limit, Integer page) {
        Page<JobsEntity> jobs = jobsRepository.findAllByUserIdOrderByIdDesc(client.getId(), new PageRequest(page, limit));
        return jobs.map(JobsParser::toDTO);
    }
}

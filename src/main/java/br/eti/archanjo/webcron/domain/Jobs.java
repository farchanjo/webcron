package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.repositories.mysql.JobsRepository;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
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
    private final UserRepository userRepository;
    private final JobsRepository jobsRepository;

    @Autowired
    public Jobs(JobsRepository jobsRepository, UserRepository userRepository) {
        this.jobsRepository = jobsRepository;
        this.userRepository = userRepository;
    }

    /**
     * @param client {@link UserDTO}
     * @param limit  {@link Integer}
     * @param page   {@link Integer}
     * @return {@link Page<JobsDTO>}
     */
    public Page<JobsDTO> listAll(UserDTO client,
                                 Integer limit, Integer page) throws Exception {
        Page<JobsEntity> jobs = jobsRepository.findAllByUserIdOrderByIdDesc(client.getId(),
                new PageRequest(page, limit));
        return jobs.map(JobsParser::toDTO);
    }

    /**
     * @param client {@link UserDTO}
     * @param job    {@link JobsDTO}
     * @return {@link JobsDTO}
     */
    public JobsDTO save(UserDTO client, JobsDTO job) throws Exception {
        JobsEntity entity;
        if (job != null && job.getId() != null) {
            entity = jobsRepository.findOne(job.getId());
            entity.setName(job.getName());
            entity.setFixedRate(job.getFixedRate());
            entity.setAsync(job.getAsync());
            entity.setCron(job.getCron());
            entity.setStatus(job.getStatus());
            entity.setUnit(job.getUnit());
        } else {
            UserEntity userEntity = userRepository.findOne(client.getId());
            entity = JobsParser.toEntity(job);
            entity.setUser(userEntity);
        }
        return JobsParser.toDTO(jobsRepository.save(entity));
    }

    /**
     * @param client {@link UserDTO}
     * @param id     {@link Long}
     * @return {@link Boolean}
     */
    public boolean delete(UserDTO client, Long id) {
        JobsEntity entity = jobsRepository.findOne(id);
        if (entity != null &&
                entity.getUser() != null &&
                entity.getUser().getId().equals(client.getId())) {
            jobsRepository.delete(id);
            return true;
        }
        return false;
    }
}

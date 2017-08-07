package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.dtos.ExecutionStatusDTO;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mongo.ExecutionStatusEntity;
import br.eti.archanjo.webcron.entities.mysql.EnvironmentEntity;
import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.exceptions.BadRequestException;
import br.eti.archanjo.webcron.quartz.QuartzService;
import br.eti.archanjo.webcron.repositories.mongo.ExecutionStatusRepository;
import br.eti.archanjo.webcron.repositories.mysql.JobsRepository;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.parsers.JobsParser;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Jobs {
    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);
    private final UserRepository userRepository;
    private final JobsRepository jobsRepository;
    private final QuartzService quartzService;
    private final ExecutionStatusRepository executionStatusRepository;

    @Autowired
    public Jobs(JobsRepository jobsRepository, UserRepository userRepository, QuartzService quartzService,
                ExecutionStatusRepository executionStatusRepository) {
        this.jobsRepository = jobsRepository;
        this.userRepository = userRepository;
        this.quartzService = quartzService;
        this.executionStatusRepository = executionStatusRepository;

    }

    /**
     * @param limit {@link Integer}
     * @param page  {@link Integer}
     * @return {@link Page<JobsDTO>}
     */
    public Page<JobsDTO> listAll(Integer limit, Integer page) throws Exception {
        Page<JobsEntity> jobs = jobsRepository.findAllByOrderByIdDesc(new PageRequest(page, limit));
        return jobs.map(JobsParser::toDTO);
    }

    public void loadAllJobsFromBase() {
        jobsRepository.findAll()
                .forEach(p -> {
                    try {
                        quartzService.saveJob(JobsParser.toDTO(p));
                        logger.info(String.format("%s job started", p.getName()));
                    } catch (BadRequestException | SchedulerException e) {
                        logger.error("Jobs{loadAllJobsFromBase}", e);
                    }
                });
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
            entity.setEnvironments(job.getEnvironments() != null ? job.getEnvironments()
                    .stream()
                    .map(p -> EnvironmentEntity.builder()
                            .key(p.getKey())
                            .value(p.getValue())
                            .build())
                    .collect(Collectors.toList()) : null);
            entity.setCommand(job.getCommand());
            entity.setDirectory(job.getDirectory());
        } else {
            UserEntity userEntity = userRepository.findOne(client.getId());
            entity = JobsParser.toEntity(job);
            entity.setUser(userEntity);
        }
        entity = jobsRepository.save(entity);
        JobsDTO dto = JobsParser.toDTO(entity);
        dto.setUserId(entity.getUser().getId());
        quartzService.saveJob(dto);
        return JobsParser.toDTO(entity);
    }

    /**
     * @param client {@link UserDTO}
     * @param id     {@link Long}
     * @return {@link Boolean}
     */
    public boolean delete(UserDTO client, Long id) throws SchedulerException {
        JobsEntity entity = jobsRepository.findOne(id);
        if (entity != null &&
                entity.getUser() != null &&
                entity.getUser().getId().equals(client.getId())) {
            jobsRepository.delete(id);
            quartzService.deleteJob(JobsParser.toDTO(entity));
            return true;
        }
        return false;
    }

    /**
     * @param client {@link UserDTO}
     * @param limit  {@link Integer}
     * @param page   {@link Integer}
     * @param name
     * @param erros
     * @return {@link Page<ExecutionStatusDTO>}
     */
    public Page<ExecutionStatusDTO> listResults(Integer limit, Integer page, String name, Boolean erros) {
        Page<ExecutionStatusEntity> executionStatusEntities;
        if (name == null) {
            if (erros != null) {
                executionStatusEntities = executionStatusRepository.findAllByErrorsOrderByCreatedDesc(erros, new PageRequest(page, limit));
            } else {
                executionStatusEntities = executionStatusRepository.findAllByOrderByCreatedDesc(new PageRequest(page, limit));
            }
        } else {
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(name);
            if (erros != null) {
                executionStatusEntities = executionStatusRepository.findAllByErrorsOrderByCreatedDesc(criteria, erros, new PageRequest(page, limit));
            } else {
                executionStatusEntities = executionStatusRepository.findByOrderByCreatedDesc(criteria, new PageRequest(page, limit));
            }
        }
        return executionStatusEntities.map(source -> ExecutionStatusDTO.builder()
                .created(source.getCreated())
                .errorMessage(source.getErrorMessage())
                .errors(source.isErrors())
                .fireTime(source.getFireTime())
                .nextFireTime(source.getNextFireTime())
                .output(source.getOutput())
                .exitCode(source.getExitCode())
                .prevFireTime(source.getPrevFireTime())
                .scheduledFireTime(source.getScheduledFireTime())
                .jobRunTime(source.getJobRunTime())
                .id(source.getId())
                .job(source.getJob())
                .build());
    }
}

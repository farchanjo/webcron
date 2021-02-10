package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.dtos.*;
import br.eti.archanjo.webcron.entities.mongo.ExecutionStatusEntity;
import br.eti.archanjo.webcron.entities.mysql.EnvironmentEntity;
import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.exceptions.BadRequestException;
import br.eti.archanjo.webcron.exceptions.NotFoundException;
import br.eti.archanjo.webcron.quartz.QuartzService;
import br.eti.archanjo.webcron.repositories.mongo.ExecutionStatusRepository;
import br.eti.archanjo.webcron.repositories.mysql.JobsRepository;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.parsers.JobsParser;
import com.newrelic.api.agent.Trace;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public Page<JobsDTO> listAll(Integer limit, Integer page) {
        Page<JobsEntity> jobs = jobsRepository.findAllByOrderByIdDesc(PageRequest.of(page, limit));
        return jobs.map(JobsParser::toDTO);
    }

    @Trace(metricName = "Jobs{loadAllJobsFromBase}", async = true, dispatcher = true)
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
        if (job == null)
            throw new BadRequestException("Missing job data");
        if (job.getId() != null) {
            Optional<JobsEntity> jobsEntity = jobsRepository.findById(job.getId());
            if (!jobsEntity.isPresent())
                throw new NotFoundException(String.format("%s cannot be found", job.getName()));
            entity = jobsEntity.get();
            entity.setName(job.getName());
            entity.setFixedRate(job.getFixedRate());
            entity.setAsync(job.getAsync());
            entity.setCron(job.getCron());
            entity.setStatus(job.getStatus());
            entity.setSystemUser(job.getSystem().getUser());
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
            Optional<UserEntity> optionalUserEntity = userRepository.findById(client.getId());
            if (!optionalUserEntity.isPresent())
                throw new NotFoundException(String.format("%s user cannot be found", client.getUsername()));
            UserEntity userEntity = optionalUserEntity.get();
            entity = JobsParser.toEntity(job);
            entity.setUser(userEntity);
        }
        entity = jobsRepository.save(entity);
        JobsDTO dto = JobsParser.toDTO(entity);
        dto.setUserId(entity.getUser().getId());
        quartzService.saveJob(job);
        return JobsParser.toDTO(entity);
    }

    /**
     * @param client {@link UserDTO}
     * @param id     {@link Long}
     * @return {@link Boolean}
     */
    public boolean delete(UserDTO client, Long id) throws SchedulerException {
        Optional<JobsEntity> entity = jobsRepository.findById(id);
        if (entity.isPresent() &&
                entity.get().getUser() != null &&
                entity.get().getUser().getId().equals(client.getId())) {
            jobsRepository.deleteById(id);
            quartzService.deleteJob(JobsParser.toDTO(entity.get()));
            return true;
        }
        return false;
    }

    /**
     * @param limit {@link Integer}
     * @param page  {@link Integer}
     * @param name  {@link String}
     * @param erros {@link Boolean}
     * @return {@link Page<ExecutionStatusDTO>}
     */
    public Page<ExecutionStatusDTO> listResults(Integer limit, Integer page, String name, Boolean erros) {
        Page<ExecutionStatusEntity> executionStatusEntities;
        if (name == null) {
            if (erros) {
                executionStatusEntities = executionStatusRepository.findAllByErrorsOrderByCreatedDesc(true, PageRequest.of(page, limit));
            } else {
                executionStatusEntities = executionStatusRepository.findAllByOrderByCreatedDesc(PageRequest.of(page, limit));
            }
        } else {
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(name);
            if (erros) {
                executionStatusEntities = executionStatusRepository.findAllByErrorsOrderByCreatedDesc(criteria, true, PageRequest.of(page, limit));
            } else {
                executionStatusEntities = executionStatusRepository.findByOrderByCreatedDesc(criteria, PageRequest.of(page, limit));
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

    /**
     * @return {@link List<RunningJobDTO>}
     */
    public List<RunningJobDTO> listRunning() {
        try {
            return quartzService.getScheduler()
                    .getCurrentlyExecutingJobs()
                    .stream()
                    .map(job -> RunningJobDTO.builder()
                            .id(job.getFireInstanceId())
                            .name(job.getJobDetail().getKey().getName())
                            .fireTime(job.getFireTime())
                            .nextFireTime(job.getNextFireTime())
                            .prevFireTime(job.getPreviousFireTime())
                            .jobRunTime(job.getJobRunTime())
                            .trigger(TriggerDTO.builder()
                                    .name(job.getTrigger().getKey().getName())
                                    .calendarName(job.getTrigger().getCalendarName())
                                    .description(job.getTrigger().getDescription())
                                    .startTime(job.getTrigger().getStartTime())
                                    .endTime(job.getTrigger().getEndTime())
                                    .priority(job.getTrigger().getPriority())
                                    .nextFireTime(job.getTrigger().getNextFireTime())
                                    .prevFireTime(job.getTrigger().getPreviousFireTime())
                                    .build())
                            .scheduledFireTime(job.getScheduledFireTime())
                            .build())
                    .collect(Collectors.toList());
        } catch (SchedulerException e) {
            logger.warn("Jobs{listRunning}", e);
            return Collections.singletonList(RunningJobDTO.builder().build());
        }
    }

    /**
     * @param runningJobDTO {@link RunningJobDTO}
     * @return {@link RunningJobDTO}
     */
    public RunningJobDTO stopRunningService(RunningJobDTO runningJobDTO) {
        try {
            quartzService.getScheduler().interrupt(runningJobDTO.getId());
        } catch (SchedulerException e) {
            logger.warn("Jobs{stopRunningService}", e);
        }
        return runningJobDTO;
    }
}

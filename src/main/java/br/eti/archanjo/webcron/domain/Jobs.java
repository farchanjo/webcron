package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.configs.PropertiesConfig;
import br.eti.archanjo.webcron.constants.QuartzContants;
import br.eti.archanjo.webcron.dtos.ExecutionStatusDTO;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mongo.ExecutionStatusEntity;
import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.exceptions.BadRequestException;
import br.eti.archanjo.webcron.exceptions.NotAuthorizedException;
import br.eti.archanjo.webcron.exceptions.NotFoundException;
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
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Jobs {
    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);
    private final UserRepository userRepository;
    private final JobsRepository jobsRepository;
    private final QuartzService quartzService;
    private final ExecutionStatusRepository executionStatusRepository;
    private final PropertiesConfig config;


    @Autowired
    public Jobs(JobsRepository jobsRepository, UserRepository userRepository, QuartzService quartzService,
                ExecutionStatusRepository executionStatusRepository, PropertiesConfig config) {
        this.jobsRepository = jobsRepository;
        this.userRepository = userRepository;
        this.quartzService = quartzService;

        /**
         * Loading all jobs
         */
        loadAllJobsFromBase();
        this.executionStatusRepository = executionStatusRepository;
        this.config = config;
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

    private void loadAllJobsFromBase() {
        jobsRepository.findAll()
                .forEach(p -> {
                    try {
                        quartzService.saveJob(JobsParser.toDTO(p));
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
            entity.setEnvironments(job.getEnvironments());
            entity.setCommand(job.getCommand());
            entity.setDirectory(job.getDirectory());
        } else {
            UserEntity userEntity = userRepository.findOne(client.getId());
            entity = JobsParser.toEntity(job);
            entity.setUser(userEntity);
        }
        entity = jobsRepository.save(entity);
        JobsDTO dto = JobsParser.toDTO(entity);
        dto.setUserId(entity.getId());
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
     * @return {@link Page<ExecutionStatusDTO>}
     */
    public Page<ExecutionStatusDTO> listResults(UserDTO client, Integer limit, Integer page) {
        Page<ExecutionStatusEntity> executionStatusEntities = executionStatusRepository.findAllByJobUserIdOrderByCreatedDesc(client.getId(),
                new PageRequest(page, limit));
        return executionStatusEntities.map(source -> ExecutionStatusDTO.builder()
                .created(source.getCreated())
                .modified(source.getModified())
                .errorMessage(source.getErrorMessage())
                .errors(source.isErrors())
                .fireTime(source.getFireTime())
                .nextFireTime(source.getNextFireTime())
                .prevFireTime(source.getPrevFireTime())
                .scheduledFireTime(source.getScheduledFireTime())
                .jobRunTime(source.getJobRunTime())
                .id(source.getId())
                .job(source.getJob())
                .build());
    }

    public String readLogFile(UserDTO client, Long jobId) throws NotFoundException, IOException {
        JobsEntity entity = jobsRepository.findOne(jobId);
        if (entity == null)
            throw new NotFoundException("This jobs does not exist");

        if (!entity.getUser().getId().equals(client.getId()))
            throw new NotAuthorizedException("You dont have permission to watch this file");

        InputStreamReader fis = new InputStreamReader(new FileInputStream(getFilePath(entity).toFile()));

        BufferedReader br = new BufferedReader(fis);
        StringBuilder buffer = new StringBuilder();
        String line;
        do {
            line = br.readLine();
            buffer.append(String.format("%s\n", line));
        } while (line != null);
        fis.close();
        return buffer.toString();
    }

    private Path getFilePath(JobsEntity job) {
        return Paths.get(config.getLogging().getFolder(), String.format(QuartzContants.FORMAT_LOG,
                job.getId(), job.getName()));
    }
}

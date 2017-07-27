package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.configs.Schedulers;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.exceptions.BadRequestException;
import br.eti.archanjo.webcron.repositories.mysql.JobsRepository;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.triggers.CustomCronTrigger;
import br.eti.archanjo.webcron.triggers.CustomPeriodicTrigger;
import br.eti.archanjo.webcron.utils.parsers.JobsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Jobs {
    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);
    private final UserRepository userRepository;
    private final JobsRepository jobsRepository;

    private final Schedulers schedulers;

    @Autowired
    public Jobs(JobsRepository jobsRepository, UserRepository userRepository, Schedulers schedulers) {
        this.jobsRepository = jobsRepository;
        this.userRepository = userRepository;
        this.schedulers = schedulers;
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
        entity = jobsRepository.save(entity);
        createJob(JobsParser.toDTO(entity));
        return JobsParser.toDTO(entity);
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

    /**
     * @param job {@link JobsDTO}
     */
    private void createJob(JobsDTO job) throws BadRequestException {
        if (!checkJobsRegistered(job)) {
            removeTask(job);

            /*
             * Choosing a Type
             */
            switch (job.getAsync()) {
                case PERIODIC:
                    schedulers.getTaskRegistrar()
                            .addTriggerTask(new Runnable() {
                                @Override
                                public void run() {
                                    logger.info("running");
                                }
                            }, new CustomPeriodicTrigger(job.getFixedRate(), job.getUnit(), job));
                    break;
                case CRON:
                    if (!CronSequenceGenerator.isValidExpression(job.getCron())) {
                        throw new BadRequestException(String.format("%s is not valid", job.getCron()));
                    }
                    schedulers.getTaskRegistrar()
                            .addTriggerTask(new Runnable() {
                                @Override
                                public void run() {
                                    logger.info("running");
                                }
                            }, new CustomCronTrigger(job.getCron(), job));
                    break;
            }
        }
    }

    /**
     * @param job {@link JobsDTO}
     * @return {@link Boolean}
     */
    private boolean checkJobsRegistered(JobsDTO job) {
        return schedulers.getTaskRegistrar()
                .getTriggerTaskList()
                .stream()
                .anyMatch(p -> {
                    if (p.getTrigger() instanceof CustomPeriodicTrigger) {
                        return ((CustomPeriodicTrigger) p.getTrigger()).getJob().equals(job);
                    } else if (p.getTrigger() instanceof CustomCronTrigger) {
                        return ((CustomCronTrigger) p.getTrigger()).getJob().equals(job);
                    }
                    return false;
                });
    }

    /**
     * @param job {@link JobsDTO}
     */
    private void removeTask(JobsDTO job) {
        List<TriggerTask> taskList = schedulers.getTaskRegistrar()
                .getTriggerTaskList()
                .stream()
                .filter(p -> {
                    if (p.getTrigger() instanceof CustomPeriodicTrigger) {
                        return ((CustomPeriodicTrigger) p.getTrigger()).getJob().equals(job);
                    } else if (p.getTrigger() instanceof CustomCronTrigger) {
                        return ((CustomCronTrigger) p.getTrigger()).getJob().equals(job);
                    }
                    return false;
                })
                .collect(Collectors.toList());
        if (taskList != null) {
            taskList.forEach(p -> schedulers.getTaskRegistrar().getTriggerTaskList().remove(p));
        }
    }
}

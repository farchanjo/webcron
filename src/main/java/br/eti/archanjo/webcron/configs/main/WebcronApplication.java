package br.eti.archanjo.webcron.configs.main;

import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.enums.AsyncType;
import br.eti.archanjo.webcron.enums.Roles;
import br.eti.archanjo.webcron.enums.Status;
import br.eti.archanjo.webcron.repositories.mysql.JobsRepository;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = "br.eti.archanjo")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400,
        redisFlushMode = RedisFlushMode.IMMEDIATE,
        redisNamespace = "webcron")
@EnableJpaRepositories("br.eti.archanjo.webcron.repositories.mysql")
@EntityScan("br.eti.archanjo.webcron.entities.mysql")
@EnableScheduling
@EnableAsync
@Transactional
public class WebcronApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final JobsRepository jobsRepository;

    @Autowired
    public WebcronApplication(UserRepository userRepository, JobsRepository jobsRepository) {
        this.userRepository = userRepository;
        this.jobsRepository = jobsRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebcronApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        UserEntity entity = UserEntity.builder()
                .status(Status.ENABLE)
                .name("Fabricio Archanjo Fonseca")
                .username("farchanjo")
                .password(HashUtils.sha256("q1w2e3"))
                .email("farchanjo@gmail.com")
                .roles(Roles.USER)
                .build();
        entity = userRepository.save(entity);

        for (int i = 0; i < 3; i++) {
            JobsEntity jobsEntity = JobsEntity.builder()
                    .name("Teste")
                    .fixedRate(10)
                    .unit(TimeUnit.DAYS)
                    .async(AsyncType.PERIODIC)
                    .status(Status.ENABLE)
                    .build();
            jobsEntity.setUser(entity);
            jobsRepository.save(jobsEntity);
        }
    }
}

package br.eti.archanjo.webcron.configs.main;

import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.enums.Roles;
import br.eti.archanjo.webcron.enums.Status;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication(scanBasePackages = "br.eti.archanjo")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400,
        redisFlushMode = RedisFlushMode.IMMEDIATE,
        redisNamespace = "webcron")
@EnableJpaRepositories("br.eti.archanjo.webcron.repositories.mysql")
@EntityScan("br.eti.archanjo.webcron.entities.mysql")
@Transactional
public class WebcronApplication implements CommandLineRunner {
    private final UserRepository userRepository;

    @Autowired
    public WebcronApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        userRepository.save(entity);
    }
}

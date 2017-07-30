package br.eti.archanjo.webcron.configs.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication(scanBasePackages = "br.eti.archanjo")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400,
        redisFlushMode = RedisFlushMode.IMMEDIATE,
        redisNamespace = "webcron")
@EnableJpaRepositories("br.eti.archanjo.webcron.repositories.mysql")
@EntityScan("br.eti.archanjo.webcron.entities.mysql")
@EnableMongoRepositories(basePackages = "br.eti.archanjo.webcron.repositories.mongo")
public class WebcronApplication implements CommandLineRunner {
    private final static Logger logger = LoggerFactory.getLogger(WebcronApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(WebcronApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}

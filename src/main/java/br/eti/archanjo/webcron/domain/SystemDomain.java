package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.configs.PropertiesConfig;
import br.eti.archanjo.webcron.dtos.SystemUsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemDomain {
    private static Pattern userFilter = Pattern.compile(":");

    private final Logger logger = LoggerFactory.getLogger(SystemDomain.class);

    private final PropertiesConfig config;

    @Autowired
    public SystemDomain(PropertiesConfig config) {
        this.config = config;
    }

    public List<SystemUsersDTO> getSysmtemUsers() throws IOException {
        if (!Files.exists(config.getSystemUserFile()))
            logger.error(String.format("%s cannot be found", config.getSystemUserFile()));

        return Files.lines(config.getSystemUserFile())
                .map(p -> userFilter.splitAsStream(p)
                        .findFirst()
                        .map(d -> SystemUsersDTO.builder().user(d).build())
                        .get())
                .filter(p -> !p.getUser().startsWith("#"))
                .collect(Collectors.toList());
    }
}

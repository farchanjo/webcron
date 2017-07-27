package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.parsers.UserParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Users {
    private final UserRepository userRepository;

    @Autowired
    public Users(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param client {@link UserDTO}
     * @return
     */
    public UserDTO me(UserDTO client) {
        return UserParser.toDTO(userRepository.findOne(client.getId()));
    }
}

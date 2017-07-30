package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.exceptions.NotFoundException;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.HashUtils;
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

    /**
     * @param client {@link UserDTO}
     * @param user   {@link UserDTO}
     * @return {@link UserDTO}
     */
    public UserDTO save(UserDTO user) throws NotFoundException {
        UserEntity entity = userRepository.findOne(user.getId());
        if (entity == null)
            throw new NotFoundException("User not found");
        
        if (user.getName() != null && !user.getName().isEmpty())
            entity.setName(user.getName());

        if (user.getPassword() != null && !user.getPassword().isEmpty())
            entity.setPassword(HashUtils.sha256(user.getPassword()));

        if (user.getEmail() != null && !user.getEmail().isEmpty())
            entity.setEmail(user.getEmail());

        if (user.getUsername() != null && !user.getUsername().isEmpty())
            entity.setUsername(user.getUsername());
        return UserParser.toDTO(userRepository.save(entity));
    }
}

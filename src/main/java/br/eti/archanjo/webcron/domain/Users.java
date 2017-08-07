package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.enums.Status;
import br.eti.archanjo.webcron.exceptions.NotFoundException;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.HashUtils;
import br.eti.archanjo.webcron.utils.parsers.UserParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        UserEntity entity;
        if (user.getId() == null) {
            entity = UserEntity.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .roles(user.getRoles())
                    .password(HashUtils.sha256(user.getPassword()))
                    .username(user.getUsername())
                    .build();
        } else {
            entity = userRepository.findOne(user.getId());
            if (user.getName() != null && !user.getName().isEmpty())
                entity.setName(user.getName());

            if (user.getPassword() != null && !user.getPassword().isEmpty())
                entity.setPassword(HashUtils.sha256(user.getPassword()));

            if (user.getEmail() != null && !user.getEmail().isEmpty())
                entity.setEmail(user.getEmail());

            if (user.getUsername() != null && !user.getUsername().isEmpty())
                entity.setUsername(user.getUsername());

            if (user.getStatus() != null)
                entity.setStatus(user.getStatus());

            if (user.getRoles() != null)
                entity.setRoles(user.getRoles());
        }
        return UserParser.toDTO(userRepository.save(entity));

    }

    /**
     * @param username {@link String}
     * @param password {@link String}
     * @return {@link UserEntity}
     */
    public UserEntity authenticate(String username, String password) {
        return userRepository.findByUsernameAndPasswordAndStatus(username, HashUtils.sha256(password), Status.ENABLE);
    }

    /**
     * @param limit {@link Integer}
     * @param page  {@link Integer}
     * @return {@link Page<UserDTO>}
     */
    public Page<UserDTO> listUsers(Integer limit, Integer page) {
        Page<UserEntity> usersPage = userRepository.findAll(new PageRequest(page, limit));
        return usersPage.map(source -> UserDTO.builder()
                .id(source.getId())
                .name(source.getName())
                .username(source.getUsername())
                .email(source.getEmail())
                .roles(source.getRoles())
                .status(source.getStatus())
                .created(source.getCreated())
                .modified(source.getModified())
                .build());
    }
}

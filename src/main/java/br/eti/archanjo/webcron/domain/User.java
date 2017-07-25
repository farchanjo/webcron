package br.eti.archanjo.webcron.domain;

import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.enums.Status;
import br.eti.archanjo.webcron.repositories.mysql.UserRepository;
import br.eti.archanjo.webcron.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class User {
    private final UserRepository userRepository;

    @Autowired
    public User(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity authenticate(String username, String password) {
        return userRepository.findByUsernameOrEmailAndPasswordAndStatus(username, username,
                HashUtils.sha256(password), Status.ENABLE);
    }
}

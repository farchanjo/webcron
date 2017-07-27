package br.eti.archanjo.webcron.facade;

import br.eti.archanjo.webcron.domain.Users;
import br.eti.archanjo.webcron.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserFacade {
    private final Users users;

    @Autowired
    public UserFacade(Users users) throws Exception {
        this.users = users;
    }

    /**
     * @param client {@link UserDTO}
     * @return {@link UserDTO}
     * @throws Exception
     */
    public UserDTO me(UserDTO client) throws Exception {
        return users.me(client);
    }
}

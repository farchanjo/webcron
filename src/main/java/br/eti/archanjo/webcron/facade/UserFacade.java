package br.eti.archanjo.webcron.facade;

import br.eti.archanjo.webcron.domain.Users;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.enums.Roles;
import br.eti.archanjo.webcron.exceptions.BadRequestException;
import br.eti.archanjo.webcron.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
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

    /**
     * @param client {@link UserDTO}
     * @param user   {@link UserDTO}
     * @return {@link UserDTO}
     */
    public UserDTO save(UserDTO client, UserDTO user) throws Exception {
        if (user == null)
            throw new BadRequestException("Missing data");
        if (client.getId().equals(user.getId())) {
            return users.save(user);
        } else {
            if (!client.getRoles().equals(Roles.ADMIN))
                throw new NotAuthorizedException("You cannot perform this action, you must be admin");
            return users.save(user);
        }
    }

    /**
     * @param client {@link UserDTO}
     * @param limit  {@link Integer}
     * @param page   {@link Integer}
     * @return {@link  Page<UserDTO>}
     */
    public Page<UserDTO> listUsers(UserDTO client, Integer limit, Integer page) {
        if (!client.getRoles().equals(Roles.ADMIN))
            throw new NotAuthorizedException("You dont have permission for that");
        return users.listUsers(limit, page);
    }
}

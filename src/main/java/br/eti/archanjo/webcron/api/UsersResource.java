package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = PathContants.USERS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UsersResource extends GenericResource {
    private final UserFacade userFacade;

    @Autowired
    public UsersResource(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @RequestMapping(path = PathContants.ME, method = RequestMethod.GET)
    public UserDTO me() throws Exception {
        return userFacade.me(getClient());
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserDTO save(@RequestBody UserDTO user) throws Exception {
        return userFacade.save(getClient(), user);
    }
}

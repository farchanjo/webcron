package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.GET)
    public Page<UserDTO> save(@RequestParam(value = "limit") Integer limit,
                              @RequestParam(value = "page") Integer page) throws Exception {
        return userFacade.listUsers(getClient(), limit, page);
    }
}

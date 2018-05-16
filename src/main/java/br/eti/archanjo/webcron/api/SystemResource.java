package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.dtos.SystemUsersDTO;
import br.eti.archanjo.webcron.facade.SystemFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = PathContants.SYSTEM, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SystemResource extends GenericResource {
    private final SystemFacade systemFacade;

    @Autowired
    public SystemResource(SystemFacade systemFacade) {
        this.systemFacade = systemFacade;
    }

    @RequestMapping(path = PathContants.USERS, method = RequestMethod.GET)
    public List<SystemUsersDTO> listSystemUsers() throws Exception {
        return systemFacade.getSysmtemUsers();
    }
}

package br.eti.archanjo.webcron.facade;

import br.eti.archanjo.webcron.domain.SystemDomain;
import br.eti.archanjo.webcron.dtos.SystemUsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemFacade {
    private final SystemDomain system;

    @Autowired
    public SystemFacade(SystemDomain system) {
        this.system = system;
    }

    /**
     * @return {@link List<SystemUsersDTO>}
     * @throws IOException
     */
    public List<SystemUsersDTO> getSysmtemUsers() throws IOException {
        return system.getSysmtemUsers();
    }
}

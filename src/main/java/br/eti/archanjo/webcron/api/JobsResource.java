package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.facade.JobsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = PathContants.JOBS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class JobsResource extends GenericResource {
    private final JobsFacade jobsFacade;

    @Autowired
    public JobsResource(JobsFacade jobsFacade) {
        this.jobsFacade = jobsFacade;
    }

    @RequestMapping(path = PathContants.ALL, method = RequestMethod.GET)
    public Page<JobsDTO> listAll(@RequestParam(value = "limit") Integer limit,
                                  @RequestParam(value = "page") Integer page) throws Exception {
        return jobsFacade.listAll(getClient(), limit, page);
    }
}

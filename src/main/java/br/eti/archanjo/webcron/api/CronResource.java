package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.facade.CronFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = PathContants.CRON, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CronResource extends GenericResource {
    private final CronFacade cronFacade;

    @Autowired
    public CronResource(CronFacade cronFacade) {
        this.cronFacade = cronFacade;
    }

    @RequestMapping(path = PathContants.JOBS, method = RequestMethod.GET)
    public Page<JobsDTO> listJobs(@RequestParam(value = "limit") Integer limit,
                                  @RequestParam(value = "page") Integer page) throws Exception {
        return cronFacade.listJobs(getClient(), limit, page);
    }
}

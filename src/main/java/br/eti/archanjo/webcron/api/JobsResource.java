package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.dtos.ExecutionStatusDTO;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.dtos.RunningJobDTO;
import br.eti.archanjo.webcron.facade.JobsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(path = PathContants.CREATE, method = RequestMethod.POST)
    public JobsDTO saveJob(@RequestBody JobsDTO job) throws Exception {
        return jobsFacade.save(getClient(), job);
    }

    @RequestMapping(path = PathContants.DELETE + "/{id}", method = RequestMethod.DELETE)
    public Boolean deleteJob(@PathVariable("id") Long id) throws Exception {
        return jobsFacade.delete(getClient(), id);
    }

    @RequestMapping(path = PathContants.RUNNNING, method = RequestMethod.GET)
    public List<RunningJobDTO> listRunningJob() throws Exception {
        return jobsFacade.listRunningJobs();
    }
    @RequestMapping(path = PathContants.RUNNNING + PathContants.STOP, method = RequestMethod.POST)
    public RunningJobDTO listRunningJob(@RequestBody RunningJobDTO runningJobDTO) throws Exception {
        return jobsFacade.stopRunningService(runningJobDTO);
    }

    @RequestMapping(path = PathContants.RESULTS, method = RequestMethod.GET)
    public Page<ExecutionStatusDTO> listExecutionStatus(@RequestParam(value = "limit") Integer limit,
                                                        @RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "erros", required = false) Boolean erros) throws Exception {
        return jobsFacade.listResults(getClient(), limit, page, name, erros);
    }
}

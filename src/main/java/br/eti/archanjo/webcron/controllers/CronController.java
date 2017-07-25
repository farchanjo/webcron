package br.eti.archanjo.webcron.controllers;

import br.eti.archanjo.webcron.dtos.CommandDTO;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class CronController {

    @MessageMapping("/listjobs")
    @SendTo("/topic/listjobs")
    public List<JobsDTO> listjobs(CommandDTO command) throws Exception {
        Thread.sleep(1000); // simulated delay
        return Arrays.asList(JobsDTO.builder()
                        .name("Teste")
                        .build(),
                JobsDTO.builder()
                        .name("Teste")
                        .build());
    }
}
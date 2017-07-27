package br.eti.archanjo.webcron.quartz.jobs;

import br.eti.archanjo.webcron.dtos.JobsDTO;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


@Getter
public class CommandLineJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(CommandLineJob.class);
    private JobsDTO job;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        feedJob(context);
        logger.debug(String.format("%s job start to run with command: %s", getJob().getName(), getJob().getCommand()));
        try {
            runCommand();
        } catch (IOException | InterruptedException e) {
            logger.warn("CommandLineJob{execute}", e);
        }
    }

    /**
     * @param context {@link JobExecutionContext}
     */
    private void feedJob(JobExecutionContext context) {
        job = (JobsDTO) context.getMergedJobDataMap().get("data");
    }

    private void runCommand() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        setEnvironments(pb);
        pb.command(getJob().getCommand().split("\\s+"));
        Process process = pb.start();
        process.waitFor();
        logger.info(IOUtils.toString(process.getInputStream()));
    }

    /**
     * @param pb {@link ProcessBuilder}
     */
    private void setEnvironments(ProcessBuilder pb) {
        if (getJob().getEnvironments() != null)
            getJob().getEnvironments().forEach(p -> pb.environment().put(p.getName(), p.getValue()));
        System.getenv().forEach((k, v) -> pb.environment().put(k, v));
        if (job.getDirectory() == null || job.getDirectory().isEmpty()) {
            pb.directory(new File(System.getProperty("user.home")));
        } else {
            pb.directory(new File(job.getDirectory()));
        }
    }
}

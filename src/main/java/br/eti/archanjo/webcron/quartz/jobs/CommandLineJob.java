package br.eti.archanjo.webcron.quartz.jobs;

import br.eti.archanjo.webcron.configs.PropertiesConfig;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import lombok.Getter;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;


@Getter
@DisallowConcurrentExecution
public class CommandLineJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(CommandLineJob.class);
    private JobsDTO job;
    private PropertiesConfig.Logging loggingConfig;

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
        loggingConfig = (PropertiesConfig.Logging) context.getMergedJobDataMap().get("loggingConfig");
    }

    private void runCommand() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        setEnvironments(pb);
        setOutputs(pb);
        pb.command(getJob().getCommand().split("\\s+"));
        Process process = pb.start();
        process.waitFor();
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

    /*
     * @param pb {@link ProcessBuilder}
     */
    private void setOutputs(ProcessBuilder pb) throws IOException {
        Path logFolder = Paths.get(getLoggingConfig().getFolder());
        if (Files.notExists(logFolder, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectories(logFolder);
            logger.info(String.format("%s folder created", logFolder.toAbsolutePath()));
        }
        Path logPath = Paths.get(getLoggingConfig().getFolder(), String.format("%s-%s-output.log", getJob().getId(), getJob().getName()));
        pb.redirectError(ProcessBuilder.Redirect.appendTo(logPath.toFile()));
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logPath.toFile()));
    }
}

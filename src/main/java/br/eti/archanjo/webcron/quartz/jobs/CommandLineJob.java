package br.eti.archanjo.webcron.quartz.jobs;

import br.eti.archanjo.webcron.configs.PropertiesConfig;
import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.pojo.JobResult;
import lombok.Getter;
import org.apache.commons.lang3.SystemUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Getter
@DisallowConcurrentExecution
public class CommandLineJob extends QuartzJobBean implements InterruptableJob {
    private final Logger logger = LoggerFactory.getLogger(CommandLineJob.class);
    private JobsDTO job;
    private Process process;
    private PropertiesConfig.Logging loggingConfig;

    @Autowired
    private PropertiesConfig config;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        feedJob(context);
        logger.debug(String.format("%s job start to run with command: %s", getJob().getName(), getJob().getCommand()));
        try {
            runCommand(context);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    /**
     * @param context {@link JobExecutionContext}
     */
    private void feedJob(JobExecutionContext context) {
        job = (JobsDTO) context.getMergedJobDataMap().get("data");
    }

    private void runCommand(JobExecutionContext context) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        setEnvironments(pb);
        Path output = setOutputs(pb);
        pb.command(getCommand(getJob().getCommand()));
        try {
            process = pb.start();
            int exitCode = process.waitFor();
            context.setResult(JobResult.builder().exitValue(exitCode).tmpFile(output).build());
        } catch (Exception e) {
            output.toFile().delete();
            throw e;
        }
    }

    private List<String> getCommand(String command) {
        List<String> commandList = new ArrayList<>();
        if (SystemUtils.IS_OS_LINUX) {
            commandList.add("su");
            commandList.add("-");
            commandList.add(getJob().getSystem().getUser());
        }
        commandList.add(config.getShell().getBin());
        commandList.add("-c");
        commandList.add(String.format("cd %s;%s", job.getDirectory(), command));
        return commandList;
    }

    /**
     * @param pb {@link ProcessBuilder}
     */
    private void setEnvironments(ProcessBuilder pb) {
        if (getJob().getEnvironments() != null)
            getJob().getEnvironments().forEach(p -> pb.environment().put(p.getKey(), p.getValue()));

        System.getenv().forEach((k, v) -> {
            if (!pb.environment().containsKey(k)) {
                pb.environment().put(k, v);
            }
        });

        if (job.getDirectory() == null || job.getDirectory().isEmpty()) {
            pb.directory(new File(System.getProperty("user.home")));
        } else {
            pb.directory(new File(job.getDirectory()));
        }
    }

    /**
     * @param pb {@link ProcessBuilder}
     */
    private Path setOutputs(ProcessBuilder pb) throws IOException {
        Path logFolder = Paths.get(config.getLogging().getFolder());
        if (Files.notExists(logFolder, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectories(logFolder);
            logger.info(String.format("%s folder created", logFolder.toAbsolutePath()));
        }

        Path tmpFile = Files.createTempFile(logFolder, "output.", ".tmp");
        pb.redirectError(ProcessBuilder.Redirect.appendTo(tmpFile.toFile()));
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(tmpFile.toFile()));
        return tmpFile;
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        process.destroy();
    }
}

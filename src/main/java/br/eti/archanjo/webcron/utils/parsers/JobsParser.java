package br.eti.archanjo.webcron.utils.parsers;


import br.eti.archanjo.webcron.dtos.JobsDTO;
import br.eti.archanjo.webcron.entities.mysql.JobsEntity;

/*
 * Created by fabricio on 11/07/17.
 */
public class JobsParser {

    public static JobsDTO toDTO(JobsEntity entity) {
        return JobsDTO.builder()
                .status(entity.getStatus())
                .name(entity.getName())
                .id(entity.getId())
                .environments(entity.getEnvironments())
                .async(entity.getAsync())
                .cron(entity.getCron())
                .fixedRate(entity.getFixedRate())
                .unit(entity.getUnit())
                .directory(entity.getDirectory())
                .command(entity.getCommand())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .build();
    }

    public static JobsEntity toEntity(JobsDTO dto) {
        return JobsEntity.builder()
                .name(dto.getName())
                .id(dto.getId())
                .status(dto.getStatus())
                .unit(dto.getUnit())
                .directory(dto.getDirectory())
                .environments(dto.getEnvironments())
                .command(dto.getCommand())
                .async(dto.getAsync())
                .fixedRate(dto.getFixedRate())
                .cron(dto.getCron())
                .created(dto.getCreated())
                .modified(dto.getModified())
                .build();
    }
}
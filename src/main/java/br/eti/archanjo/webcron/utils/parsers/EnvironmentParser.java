package br.eti.archanjo.webcron.utils.parsers;


import br.eti.archanjo.webcron.entities.mysql.EnvironmentEntity;
import br.eti.archanjo.webcron.pojo.EnvironmentDTO;

/*
 * Created by fabricio on 11/07/17.
 */
public class EnvironmentParser {

    public static EnvironmentDTO toDTO(EnvironmentEntity entity) {
        return EnvironmentDTO.builder()
                .key(entity.getKey())
                .value(entity.getValue())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .build();
    }

    public static EnvironmentEntity toEntity(EnvironmentDTO dto) {
        return EnvironmentEntity.builder()
                .key(dto.getKey())
                .value(dto.getValue())
                .created(dto.getCreated())
                .modified(dto.getModified())
                .build();
    }
}
package br.eti.archanjo.webcron.utils.parsers;


import br.eti.archanjo.webcron.dtos.UserDTO;
import br.eti.archanjo.webcron.entities.mysql.UserEntity;

/*
 * Created by fabricio on 11/07/17.
 */
public class UserParser {

    public static UserDTO toDTO(UserEntity entity) {
        return UserDTO.builder()
                .username(entity.getUsername())
                .status(entity.getStatus())
                .name(entity.getName())
                .id(entity.getId())
                .email(entity.getEmail())
                .roles(entity.getRoles())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .build();
    }

    public static UserEntity toEntity(UserDTO dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .status(dto.getStatus())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .roles(dto.getRoles())
                .id(dto.getId())
                .created(dto.getCreated())
                .modified(dto.getModified())
                .build();
    }
}
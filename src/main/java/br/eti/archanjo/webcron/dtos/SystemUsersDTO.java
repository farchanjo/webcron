package br.eti.archanjo.webcron.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
@ToString
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemUsersDTO implements Serializable {
    private static final long serialVersionUID = -9010219610094736392L;
    private String user;
}

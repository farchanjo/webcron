package br.eti.archanjo.webcron.dtos;

import br.eti.archanjo.webcron.enums.Roles;
import br.eti.archanjo.webcron.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/*
 * Created by fabricio on 10/07/17.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 7944032954933121911L;
    private Long id;
    private String name;
    private String email;
    private String username;
    private Status status;
    private Date created;
    private Date modified;
    private Roles roles;
    private String password;
}
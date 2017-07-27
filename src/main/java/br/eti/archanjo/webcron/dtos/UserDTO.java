package br.eti.archanjo.webcron.dtos;

import br.eti.archanjo.webcron.enums.Roles;
import br.eti.archanjo.webcron.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", modified=" + modified +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return new EqualsBuilder()
                .append(id, userDTO.id)
                .append(name, userDTO.name)
                .append(email, userDTO.email)
                .append(username, userDTO.username)
                .append(status, userDTO.status)
                .append(created, userDTO.created)
                .append(modified, userDTO.modified)
                .append(roles, userDTO.roles)
                .append(password, userDTO.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(email)
                .append(username)
                .append(status)
                .append(created)
                .append(modified)
                .append(roles)
                .append(password)
                .toHashCode();
    }
}
package br.eti.archanjo.webcron.entities.mysql;

import br.eti.archanjo.webcron.enums.Roles;
import br.eti.archanjo.webcron.enums.Status;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/*
 * Created by fabricio on 10/07/17.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Table(name = "USERS")
@Entity
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -6046209000285746209L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "role")
    private Roles roles;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "created")
    private Calendar created;

    @Column(name = "modified")
    private Calendar modified;

    @PrePersist
    private void prePersist() {
        created = Calendar.getInstance();
        modified = Calendar.getInstance();
    }

    @PostUpdate
    private void postUpdated() {
        modified = Calendar.getInstance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(password, that.password)
                .append(email, that.email)
                .append(username, that.username)
                .append(status, that.status)
                .append(created, that.created)
                .append(modified, that.modified)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(password)
                .append(email)
                .append(username)
                .append(status)
                .append(created)
                .append(modified)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
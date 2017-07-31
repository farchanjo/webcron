package br.eti.archanjo.webcron.entities.mysql;

import lombok.*;

import javax.persistence.*;
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
@Table(name = "ENVS")
@Entity(name = "ENVS")
public class EnvironmentEntity implements Serializable {
    private static final long serialVersionUID = -3651782794702121063L;
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "`key`", nullable = false)
    private String key;

    @Column(name = "`value`", nullable = false)
    private String value;

    @Column(name = "created", nullable = false)
    private Date created;

    @Column(name = "modified", nullable = false)
    private Date modified;

    @PrePersist
    private void prePersist() {
        created = new Date();
        modified = new Date();
    }

    @PreUpdate
    private void postUpdated() {
        modified = new Date();
    }

}
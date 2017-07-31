package br.eti.archanjo.webcron.entities.mysql;

import br.eti.archanjo.webcron.enums.AsyncType;
import br.eti.archanjo.webcron.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * Created by fabricio on 10/07/17.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Table(name = "JOBS")
@Entity(name = "JOBS")
public class JobsEntity implements Serializable {
    private static final long serialVersionUID = 6762737183310441346L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "async", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AsyncType async;

    @Column(name = "fixedRate")
    private Integer fixedRate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "unit")
    private TimeUnit unit;

    @Column(name = "cron")
    private String cron;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EnvironmentEntity> environments;

    @Column(name = "directory")
    private String directory;

    @Column(name = "command", columnDefinition = "TEXT", nullable = false)
    private String command;

    @Column(name = "created", nullable = false)
    private Date created;

    @Column(name = "modified", nullable = false)
    private Date modified;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    private UserEntity user;

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
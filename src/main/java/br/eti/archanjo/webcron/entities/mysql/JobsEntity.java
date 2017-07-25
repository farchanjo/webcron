package br.eti.archanjo.webcron.entities.mysql;

import br.eti.archanjo.webcron.enums.AsyncType;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
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

    @Column(name = "unit")
    private TimeUnit unit;

    @Column(name = "cron")
    private String cron;

    @Column(name = "created", nullable = false)
    private Calendar created;

    @Column(name = "modified", nullable = false)
    private Calendar modified;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserEntity user;

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
    public String toString() {
        return "JobsEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", async=" + async +
                ", fixedRate=" + fixedRate +
                ", cron='" + cron + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JobsEntity that = (JobsEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(async, that.async)
                .append(fixedRate, that.fixedRate)
                .append(cron, that.cron)
                .append(created, that.created)
                .append(modified, that.modified)
                .append(user, that.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(async)
                .append(fixedRate)
                .append(cron)
                .append(created)
                .append(modified)
                .append(user)
                .toHashCode();
    }
}
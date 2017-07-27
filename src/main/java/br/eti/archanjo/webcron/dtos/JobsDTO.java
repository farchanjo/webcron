package br.eti.archanjo.webcron.dtos;

import br.eti.archanjo.webcron.enums.AsyncType;
import br.eti.archanjo.webcron.enums.Status;
import br.eti.archanjo.webcron.pojo.Environment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobsDTO implements Serializable {
    private static final long serialVersionUID = -7517693919191046339L;
    private Long id;
    private String name;
    private AsyncType async;
    private Integer fixedRate;
    private Status status;
    private List<Environment> environments;
    private TimeUnit unit;
    private String cron;
    private Date created;
    private Date modified;

    @Override
    public String toString() {
        return "JobsDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", async=" + async +
                ", fixedRate=" + fixedRate +
                ", status=" + status +
                ", unit=" + unit +
                ", cron='" + cron + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JobsDTO jobsDTO = (JobsDTO) o;

        return new EqualsBuilder()
                .append(id, jobsDTO.id)
                .append(name, jobsDTO.name)
                .append(async, jobsDTO.async)
                .append(fixedRate, jobsDTO.fixedRate)
                .append(status, jobsDTO.status)
                .append(unit, jobsDTO.unit)
                .append(cron, jobsDTO.cron)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(async)
                .append(fixedRate)
                .append(status)
                .append(unit)
                .append(cron)
                .toHashCode();
    }
}
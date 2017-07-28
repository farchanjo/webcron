package br.eti.archanjo.webcron.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionStatusDTO implements Serializable {
    private static final long serialVersionUID = 9101390219113817697L;
    private String id;
    private Date fireTime;
    private Date prevFireTime;
    private Date nextFireTime;
    private Long jobRunTime;
    private Date scheduledFireTime;
    private JobsDTO job;
    private boolean errors;
    private String errorMessage;
    private Date created;
    private String output;
    private Date modified;

    @Override
    public String toString() {
        return "ExecutionStatusDTO{" +
                "id='" + id + '\'' +
                ", fireTime=" + fireTime +
                ", prevFireTime=" + prevFireTime +
                ", nextFireTime=" + nextFireTime +
                ", jobRunTime=" + jobRunTime +
                ", scheduledFireTime=" + scheduledFireTime +
                ", job=" + job +
                ", errors=" + errors +
                ", errorMessage='" + errorMessage + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ExecutionStatusDTO that = (ExecutionStatusDTO) o;

        return new EqualsBuilder()
                .append(errors, that.errors)
                .append(id, that.id)
                .append(fireTime, that.fireTime)
                .append(prevFireTime, that.prevFireTime)
                .append(nextFireTime, that.nextFireTime)
                .append(jobRunTime, that.jobRunTime)
                .append(scheduledFireTime, that.scheduledFireTime)
                .append(job, that.job)
                .append(errorMessage, that.errorMessage)
                .append(created, that.created)
                .append(modified, that.modified)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(fireTime)
                .append(prevFireTime)
                .append(nextFireTime)
                .append(jobRunTime)
                .append(scheduledFireTime)
                .append(job)
                .append(errors)
                .append(errorMessage)
                .append(created)
                .append(modified)
                .toHashCode();
    }
}

package br.eti.archanjo.webcron.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
@ToString
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RunningJobDTO implements Serializable {
    private static final long serialVersionUID = -5494245960892592296L;
    private String name;
    private String id;
    private TriggerDTO trigger;
    private long jobRunTime;
    private Date fireTime;
    private Date prevFireTime;
    private Date nextFireTime;
    private Date scheduledFireTime;
}

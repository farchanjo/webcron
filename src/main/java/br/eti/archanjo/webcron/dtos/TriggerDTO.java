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
public class TriggerDTO implements Serializable {
    private static final long serialVersionUID = 1477403856131265561L;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Date prevFireTime;
    private Date nextFireTime;
    private int priority;
    private String calendarName;
}

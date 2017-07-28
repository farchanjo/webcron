package br.eti.archanjo.webcron.entities.mongo;

import br.eti.archanjo.webcron.dtos.JobsDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Document(collection = "executions")
public class ExecutionStatusEntity implements Serializable {
    private static final long serialVersionUID = 5211167467012607987L;
    @Id
    private String id;
    @Field("fireTime")
    private Date fireTime;
    @Field("prevFireTime")
    private Date prevFireTime;
    @Field("nextFireTime")
    private Date nextFireTime;
    @Field("jobRunTime")
    private Integer jobRunTime;
    @Field("scheduledFireTime")
    private Date scheduledFireTime;
    @Field("job")
    private JobsDTO job;
    @Field("errors")
    private boolean errors = false;
    @Field("errormessage")
    private String errorMessage;
    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date modified;
}

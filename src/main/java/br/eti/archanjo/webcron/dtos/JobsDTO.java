package br.eti.archanjo.webcron.dtos;

import br.eti.archanjo.webcron.enums.AsyncType;
import br.eti.archanjo.webcron.enums.Status;
import br.eti.archanjo.webcron.pojo.EnvironmentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.index.TextIndexed;

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
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobsDTO implements Serializable {
    private static final long serialVersionUID = -7517693919191046339L;
    private Long id;
    @TextIndexed
    private String name;
    private AsyncType async;
    private Integer fixedRate;
    private Status status;
    private List<EnvironmentDTO> environments;
    private TimeUnit unit;
    private SystemUsersDTO system;
    private String command;
    private String cron;
    private Long userId;
    private String directory;
    private Date created;
    private Date modified;
}
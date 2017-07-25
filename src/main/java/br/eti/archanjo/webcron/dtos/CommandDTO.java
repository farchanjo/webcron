package br.eti.archanjo.webcron.dtos;

import br.eti.archanjo.webcron.enums.CommandType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommandDTO implements Serializable {
    private static final long serialVersionUID = 3750595497526213488L;
    private CommandType type;
}

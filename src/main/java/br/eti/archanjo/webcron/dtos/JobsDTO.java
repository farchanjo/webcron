package br.eti.archanjo.webcron.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

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
    private static final long serialVersionUID = 7614951952691851020L;
    private Long id;
    private String name;
}
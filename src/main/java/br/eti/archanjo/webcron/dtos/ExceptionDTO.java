package br.eti.archanjo.webcron.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDTO {

    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ExceptionDTO(int status, String error, String message) {
        timestamp = new Date();
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
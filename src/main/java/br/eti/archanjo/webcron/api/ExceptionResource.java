package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.dtos.ExceptionDTO;
import br.eti.archanjo.webcron.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
class ExceptionResource {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionResource.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDTO handleException(Exception e) {
        return handleGenericException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDTO handleException(BadRequestException e) {
        return handleGenericException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_REQUIRED)
    @ResponseBody
    public ExceptionDTO handleException(ServletRequestBindingException e) {
        return handleGenericException(e, HttpStatus.PRECONDITION_REQUIRED);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ExceptionDTO handleException(AlreadyExistsException e) {
        return handleGenericException(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AlreadyOnLimitException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ExceptionDTO handleException(AlreadyOnLimitException e) {
        return handleGenericException(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ExceptionDTO handleException(NotAuthorizedException e) {
        return handleGenericException(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ExceptionDTO handleException(ConflictException e) {
        return handleGenericException(e, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionDTO handleException(ServerErrorException e) {
        return handleGenericException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionDTO handleException(NotFoundException e) {
        return handleGenericException(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AssertionError.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public ExceptionDTO handleException(AssertionError e) {
        return handleGenericException(e, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.PRECONDITION_REQUIRED)
    public ExceptionDTO handleException(IllegalArgumentException e) {
        return handleGenericException(e, HttpStatus.PRECONDITION_REQUIRED);
    }

    private ExceptionDTO handleGenericException(Throwable e, HttpStatus status) {
        return new ExceptionDTO(status.value(), status.getReasonPhrase(), e.getMessage());
    }
}
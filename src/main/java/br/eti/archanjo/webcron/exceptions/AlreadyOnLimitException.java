package br.eti.archanjo.webcron.exceptions;

public class AlreadyOnLimitException extends Exception {

    public AlreadyOnLimitException(String message) {
        super(message);
    }
}
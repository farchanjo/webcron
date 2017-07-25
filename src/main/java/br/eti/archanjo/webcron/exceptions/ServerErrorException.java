package br.eti.archanjo.webcron.exceptions;

public class ServerErrorException extends Exception {

    public ServerErrorException(String message) {
        super(message);
    }
}
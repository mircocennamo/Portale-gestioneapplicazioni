package it.interno.gestioneapplicazioni.exception;

public class GroupsDuplicatedException extends RuntimeException{
    public GroupsDuplicatedException(String message) {
        super(message);
    }
}

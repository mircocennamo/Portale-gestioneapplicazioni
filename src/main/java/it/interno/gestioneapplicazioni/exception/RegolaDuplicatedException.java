package it.interno.gestioneapplicazioni.exception;

public class RegolaDuplicatedException extends RuntimeException{
    public RegolaDuplicatedException(String message) {
        super(message);
    }
}

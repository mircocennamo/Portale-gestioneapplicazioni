package it.interno.gestioneapplicazioni.exception;

public class DateNotValidException extends RuntimeException{

    public DateNotValidException(String message){
        super(message);
    }
}

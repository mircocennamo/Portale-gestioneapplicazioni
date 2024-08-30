package it.interno.gestioneapplicazioni.exception;

public class EmptyListException extends RuntimeException{

    private static final String MESSAGGIO_DEFAULT = "La lista non può essere vuota";

    public EmptyListException(){
        super(MESSAGGIO_DEFAULT);
    }

    public EmptyListException(String message){
        super(message);
    }
}

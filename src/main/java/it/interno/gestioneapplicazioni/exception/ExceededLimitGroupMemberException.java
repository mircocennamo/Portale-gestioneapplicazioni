package it.interno.gestioneapplicazioni.exception;

public class ExceededLimitGroupMemberException extends RuntimeException{

    public ExceededLimitGroupMemberException(String message){
        super(message);
    }
}

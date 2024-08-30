package it.interno.gestioneapplicazioni.exception;

public class ApplicazioneNotFoundException extends RuntimeException{

    public ApplicazioneNotFoundException(String message){
        super(message);
    }
}

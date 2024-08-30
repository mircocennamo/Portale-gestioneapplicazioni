package it.interno.gestioneapplicazioni.service;

import java.sql.Timestamp;

public interface ApplicazioneMotivMembersService {

    void deleteByAppId(String appId,String utenteCancellazione, String ufficioCancellazione,Timestamp data);

    void deleteByAppIdAndUtente(String appId, String utente, String utenteCancellazione, String ufficioCancellazione, Timestamp data);

}

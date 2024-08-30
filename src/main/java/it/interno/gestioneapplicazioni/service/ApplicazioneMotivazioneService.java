package it.interno.gestioneapplicazioni.service;

import java.sql.Timestamp;

public interface ApplicazioneMotivazioneService {

    void deleteByAppId(String appId,String utenteCancellazione, String ufficioCancellazione,Timestamp data);

}

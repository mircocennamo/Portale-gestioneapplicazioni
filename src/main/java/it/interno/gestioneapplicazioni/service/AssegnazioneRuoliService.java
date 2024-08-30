package it.interno.gestioneapplicazioni.service;

import it.interno.gestioneapplicazioni.dto.AssegnazioneRuoliDto;
import it.interno.gestioneapplicazioni.dto.PaginazioneDto;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface AssegnazioneRuoliService {
    Page<AssegnazioneRuoliDto> getListaApplicazioniAssegnabili(String codiceUtente, Integer flagRicerca, String parametroRicerca, Integer ambito, PaginazioneDto paginazione,String oamRemoteUser);


    void deleteAll(String idApplicazione, String utente, String ufficio, Timestamp data);

     void deleteAll(String codiceRuolo,String idApplicazione,String utente, String ufficio, Timestamp data);

    AssegnazioneRuoliDto salvataggioRuoliAdUtente(AssegnazioneRuoliDto input, String utente, String ufficio, Timestamp data);
}

package it.interno.gestioneapplicazioni.service;

import it.interno.gestioneapplicazioni.dto.DuplicazioneRegoleDto;
import it.interno.gestioneapplicazioni.dto.RegolaSicurezzaDto;

import java.sql.Timestamp;
import java.util.List;

public interface RegolaSicurezzaService {
    List<RegolaSicurezzaDto> getAllRegoleByRuolo(String nomeRuolo, String appId);
    RegolaSicurezzaDto getRegolaByRuoloAndIdRegola(String nomeRuolo, String appId, Integer idRegola);
    List<RegolaSicurezzaDto> salvataggio(List<RegolaSicurezzaDto> input, String utente, String ufficio, Timestamp data);
    void deleteAll(String nomeRuolo, String idApplicazione, String utente, String ufficio, Timestamp data);

    void deleteAll(String idApplicazione, String utente, String ufficio, Timestamp data);

    void duplicazioneRegole(DuplicazioneRegoleDto input, String utente, String ufficio, Timestamp data);
}

package it.interno.gestioneapplicazioni.service;

import it.interno.gestioneapplicazioni.dto.ApplicazioneDto;
import it.interno.gestioneapplicazioni.dto.filter.ApplicazioneFilterDto;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;

public interface ApplicazioneService {

    ApplicazioneDto getById(String idApplicazione);
    ApplicazioneDto getByNome(String nome);
    List<ApplicazioneDto> getAllApplicazioni();
    List<ApplicazioneDto> getAllApplicazioniByAmbito(Integer idAmbito);
    Page<ApplicazioneDto> searchAndPaginate(ApplicazioneFilterDto filtro);
    ApplicazioneDto insertNew(ApplicazioneDto input, String utenteInserimento, String ufficioInserimento, Timestamp data);
    ApplicazioneDto updateApplicazione(ApplicazioneDto input, String utenteAggiornamento, String ufficioAggiornamento, Timestamp data);
    ApplicazioneDto deleteApplicazione(String idApplicazione, String utenteCancellazione, String ufficioCancellazione, Timestamp data);
    List<ApplicazioneDto> ordinamentoApplicazioni(List<ApplicazioneDto> applicazioni, String utente, String ufficio, Timestamp data);
    List<ApplicazioneDto> searchApplicationsForAggregation();

}

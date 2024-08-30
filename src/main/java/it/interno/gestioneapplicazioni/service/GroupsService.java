package it.interno.gestioneapplicazioni.service;

import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.dto.RuoliPerRegoleSicurezzaDto;
import it.interno.gestioneapplicazioni.dto.filter.ApplicazioneFilterDto;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;

public interface GroupsService {
    List<GroupsDto> findAllByAppId(String idApp);
    List<GroupsDto> salvataggio(List<GroupsDto> input, String utente, String ufficio, Timestamp data);
    void deleteAllByApp(String appId, String utente, String ufficio, Timestamp data);
    List<RuoliPerRegoleSicurezzaDto> findAllByAutocomplete(String parametroRicerca, List<String> ruoliDaEscludere);
    void updateGroupNameAfterApplicazioneNameUpdate(String appNameOld, String appNameNew, String appId, String utente, String ufficio, Timestamp data);
    Page<RuoliPerRegoleSicurezzaDto> searchAndPaginateRegoleSicurezza(int flagRicerca, String parametroRicerca, ApplicazioneFilterDto filtro);
}

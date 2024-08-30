package it.interno.gestioneapplicazioni.service;

import it.interno.gestioneapplicazioni.dto.GroupsAggregazioneDto;
import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.dto.PaginazioneDto;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;

public interface GroupsAggregazioneService {

    Page<GroupsAggregazioneDto> getListaRuoliAggregati(PaginazioneDto paginazione);
    List<GroupsAggregazioneDto> getRuoliAggregatiByPrincipale(String applicazionePrincipale, String ruoloPrincipale);
    GroupsAggregazioneDto getDettaglioAggregazione(String applicazionePrincipale, String ruoloPrincipale, String appDipendente, String ruoloDipendente);
    List<GroupsDto> getRuoliPrincipaliAggregazione(String appId);
    List<GroupsDto> getRuoliDipendentiAggregazione(String appId, String appIdPrincipale, String ruoloPrincipale);
    GroupsAggregazioneDto inserimento(GroupsAggregazioneDto input, String utente, String ufficio, Timestamp data);
    GroupsAggregazioneDto aggiornamento(GroupsAggregazioneDto input, String utente, String ufficio,String idAppDipendente,String ruoloDipendente, Timestamp data);
    void cancellazione(String appPrincipale, String ruoloPrincipale, String appDipendente, String ruoloDipendente, String utente, String ufficio, Timestamp data);

}

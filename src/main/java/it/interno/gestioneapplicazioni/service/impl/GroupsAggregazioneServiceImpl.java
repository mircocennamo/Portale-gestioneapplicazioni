package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.dto.GroupsAggregazioneDto;
import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.dto.PaginazioneDto;
import it.interno.gestioneapplicazioni.entity.GroupsAggregazione;
import it.interno.gestioneapplicazioni.entity.key.GroupsAggregazioneKey;
import it.interno.gestioneapplicazioni.mapper.GroupsAggregazioneMapper;
import it.interno.gestioneapplicazioni.mapper.GroupsMapper;
import it.interno.gestioneapplicazioni.repository.ApplicazioneRepository;
import it.interno.gestioneapplicazioni.repository.GroupsAggregazioneRepository;
import it.interno.gestioneapplicazioni.repository.GroupsRepository;
import it.interno.gestioneapplicazioni.service.GroupsAggregazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupsAggregazioneServiceImpl implements GroupsAggregazioneService {

    @Autowired
    private GroupsAggregazioneRepository groupsAggregazioneRepository;
    @Autowired
    private GroupsAggregazioneMapper groupsAggregazioneMapper;
    @Autowired
    private ApplicazioneRepository applicazioneRepository;
    @Autowired
    private GroupsRepository groupsRepository;

    @Override
    public Page<GroupsAggregazioneDto> getListaRuoliAggregati(PaginazioneDto paginazione) {

        Pageable pageable = PageRequest.of(
                paginazione.getPageNumber(),
                paginazione.getPageSize(),
                Sort.by(
                        new Sort.Order(Sort.Direction.fromString(paginazione.getSortDirection()), paginazione.getSortBy()).ignoreCase()
                )
        );

        Page<GroupsAggregazione> result;
        if(paginazione.getSortBy().equalsIgnoreCase("APP_NAME"))
            result = groupsAggregazioneRepository.getListaRuoliAggregatiOrderByApplicazione(pageable);
        else
            result = groupsAggregazioneRepository.getListaRuoliAggregatiOrderByRuolo(pageable);

        List<GroupsAggregazioneDto> dtos = result.stream().map(el -> groupsAggregazioneMapper.toDto(el, applicazioneRepository)).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, result.getTotalElements());
    }

    @Override
    public List<GroupsAggregazioneDto> getRuoliAggregatiByPrincipale(String applicazionePrincipale, String ruoloPrincipale) {
        return groupsAggregazioneRepository.getAggregazioneByPrincipale(applicazionePrincipale,ruoloPrincipale)
                .stream()
                .map(el -> groupsAggregazioneMapper.toDto(el, applicazioneRepository))
                .toList();
    }

    @Override
    public GroupsAggregazioneDto getDettaglioAggregazione(String applicazionePrincipale, String ruoloPrincipale, String appDipendente, String ruoloDipendente) {
        return groupsAggregazioneMapper.toDto(groupsAggregazioneRepository.getDettaglioAggregazione(applicazionePrincipale, ruoloPrincipale, appDipendente, ruoloDipendente), applicazioneRepository);
    }

    @Override
    public List<GroupsDto> getRuoliPrincipaliAggregazione(String appId) {
        return groupsRepository.getRuoliPrincipaliAggregazione(appId)
                .stream()
                .map(GroupsMapper::toDto)
                .toList();
    }

    @Override
    public List<GroupsDto> getRuoliDipendentiAggregazione(String appId, String appIdPrincipale, String ruoloPrincipale) {
        return groupsRepository.getRuoliDipendentiAggregazione(appId, appIdPrincipale, ruoloPrincipale)
                .stream()
                .map(GroupsMapper::toDto)
                .toList();
    }

    // CHIAMATA INSERIMENTO
    @Override
    @Transactional
    public GroupsAggregazioneDto inserimento(GroupsAggregazioneDto input, String utente, String ufficio, Timestamp data){

        input.setDataInserimento(data);
        input.setUtenteInserimento(utente);
        input.setUfficioInserimento(ufficio);

        return groupsAggregazioneMapper.toDto(groupsAggregazioneRepository.save(groupsAggregazioneMapper.toEntity(input)), applicazioneRepository);
    }

    // CHIAMATA AGGIORNAMENTO
    @Override
    @Transactional
    public GroupsAggregazioneDto aggiornamento(GroupsAggregazioneDto input, String utente, String ufficio,String idAppDipendente,String ruoloDipendente, Timestamp data){

        this.cancellazione(input.getIdAppPrincipale(), input.getRuoloPrincipale(), idAppDipendente, ruoloDipendente , utente, ufficio, data);
        return this.inserimento(input, utente, ufficio, data);
    }

    // CHIAMATA CANCELLAZIONE
    @Override
    @Transactional
    public void cancellazione(String appPrincipale, String ruoloPrincipale, String appDipendente, String ruoloDipendente, String utente, String ufficio, Timestamp data){



        GroupsAggregazione ruoloAggregato = groupsAggregazioneRepository.findById(new GroupsAggregazioneKey(ruoloPrincipale, appPrincipale, ruoloDipendente, appDipendente)).orElse(null);

        if(ruoloAggregato == null)
            return;

        ruoloAggregato.setUtenteCancellazione(utente);
        ruoloAggregato.setUfficioCancellazione(ufficio);
        ruoloAggregato.setDataCancellazione(data);

        groupsAggregazioneRepository.save(ruoloAggregato);
    }

}
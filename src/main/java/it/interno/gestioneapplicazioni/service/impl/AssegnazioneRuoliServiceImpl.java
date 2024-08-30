package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.client.OimClient;
import it.interno.gestioneapplicazioni.dto.AssegnazioneRuoliDto;
import it.interno.gestioneapplicazioni.dto.PaginazioneDto;
import it.interno.gestioneapplicazioni.entity.*;
import it.interno.gestioneapplicazioni.exception.ExceededLimitGroupMemberException;
import it.interno.gestioneapplicazioni.repository.*;
import it.interno.gestioneapplicazioni.service.AssegnazioneRuoliService;
import it.sdi.utility.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class AssegnazioneRuoliServiceImpl implements AssegnazioneRuoliService {

    @Autowired
    private GroupMembersRepository groupMembersRepository;
    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private ApplicazioneRepository applicazioneRepository;
    @Autowired
    private TipoMotivazioneRepository tipoMotivazioneRepository;
    @Autowired
    private ApplicMotivMembersRepository applicMotivMembersRepository;
    @Autowired
    private GroupsAggregazioneRepository groupsAggregazioneRepository;
    @Autowired
    private OimClient oimClient;

    @Autowired
    private Executor asyncTaskExecutor;

    @Value("${limit.remove.group.members}")
    Integer limitRemoveGroupMembers;

    @Override
    public Page<AssegnazioneRuoliDto> getListaApplicazioniAssegnabili(String codiceUtente, Integer flagRicerca, String parametroRicerca, Integer ambito, PaginazioneDto paginazione) {

        Pageable pageable = PageRequest.of(
                paginazione.getPageNumber(),
                paginazione.getPageSize(),
                Sort.by(
                        new Sort.Order(Sort.Direction.fromString(paginazione.getSortDirection()), paginazione.getSortBy()).ignoreCase()
                )
        );

        List<Applicazione> applicazioni = applicazioneRepository.getApplicazioniPerAssegnazioneConRuoliAssegnabili(
                codiceUtente, flagRicerca, StringUtils.isBlank(parametroRicerca) ? "%" : "%" + parametroRicerca + "%", ambito, pageable);

        Integer totalElements = applicazioneRepository.countTotalApplicazioniAssegnabili(codiceUtente, flagRicerca, StringUtils.isBlank(parametroRicerca) ? "" : parametroRicerca, ambito);

        List<AssegnazioneRuoliDto> results = new ArrayList<>();
        applicazioni.forEach(el -> {

            AssegnazioneRuoliDto temp = new AssegnazioneRuoliDto(codiceUtente, el.getAppId(), el.getAppName(), el.getAppDescription(), el.getAppScope());

            // GET RUOLI DELL'APPLICAZIONE
            List<Groups> ruoli = groupsRepository.findAllAssegnabili(el.getAppId(), codiceUtente);
            temp.setRuoliAssegnabili(ruoli, codiceUtente, groupMembersRepository);
            // GET MOTIVAZIONI DELL'APPLICAZIONE
            List<TipoMotivazione> tipiMotivazione = tipoMotivazioneRepository.getAllByApp(el.getAppId());
            temp.setMotivazioniAssegnabili(tipiMotivazione, codiceUtente, applicMotivMembersRepository);

            results.add(temp);
        });

        return new PageImpl<>(results, pageable, totalElements);
    }

    @Override
    public void deleteAll(String idApplicazione,String utente, String ufficio, Timestamp data){
        List<GroupMembers> associazioni = groupMembersRepository.getByIdApplicazione(idApplicazione);
        if(associazioni!=null && associazioni.size()>limitRemoveGroupMembers){
            throw new ExceededLimitGroupMemberException("Cancellazione massiva della associazione ruoli utente non possibile,superato limite di " + limitRemoveGroupMembers + " elementi");
        }

        // Cancellazione
        associazioni.forEach(el -> {
            el.setUtenteCancellazione(utente);
            el.setUfficioCancellazione(ufficio);
            el.setDataCancellazione(data);
        });
        groupMembersRepository.saveAll(associazioni);

        Map<String,List<String>> relazioni = associazioni.stream().collect(Collectors.groupingBy(
                        GroupMembers::getNomeRuolo,Collectors.mapping(GroupMembers::getNomeUtente,Collectors.toList())));
        relazioni.keySet().stream().forEach(
                nomeruolo->{
                    oimClient.rimozioneRuoloAUtenti(nomeruolo,relazioni.get(nomeruolo));
                }
        );
     }


    public void deleteAll(String codiceRuolo,String idApplicazione,String utente, String ufficio, Timestamp data){
        List<GroupMembers> associazioni =   groupMembersRepository.getByRuolo(codiceRuolo, idApplicazione);
        if(associazioni!=null && associazioni.size()>limitRemoveGroupMembers){
            throw new ExceededLimitGroupMemberException("Cancellazione massiva della associazione ruoli utente non possibile,superato limite di " + limitRemoveGroupMembers + " elementi");
        }
        delete( associazioni, utente ,ufficio, data, codiceRuolo);
    }

    private void delete(List<GroupMembers> associazioni,String utente, String ufficio, Timestamp data,String codiceRuolo){
        // Cancellazione
        associazioni.forEach(el -> {
            el.setUtenteCancellazione(utente);
            el.setUfficioCancellazione(ufficio);
            el.setDataCancellazione(data);
        });
        groupMembersRepository.saveAll(associazioni);

       //List<GroupMembers> groupMembers =  groupMembersRepository.saveAll(associazioni);

      /* Collection<List<GroupMembers>> collection =  Util.partitionBasedOnSize(associazioni, 3);

       collection.stream().forEach(groupMembersList->{
            asyncTaskExecutor.execute(() -> {
                groupMembersList.stream().forEach(group -> {
                   oimClient.rimozioneRuolo(group.getNomeUtente(),group.getNomeRuolo());
                });
            });
        });*/

       List<String> nomiutente =  associazioni.stream()
                .map(GroupMembers::getNomeUtente)
                .toList();
        oimClient.rimozioneRuoloAUtenti(codiceRuolo,nomiutente);


    }


    @Override
    @Transactional
    public AssegnazioneRuoliDto salvataggioRuoliAdUtente(AssegnazioneRuoliDto input, String utenteOperatore, String ufficioOperatore, Timestamp dataOperazione) {

        List<GroupMembers> ruoliAssegnatiSuDb = groupMembersRepository.getByUtente(input.getCodiceUtente());
        List<ApplicMotivMembers> applicMotivMembersSuDB = applicMotivMembersRepository.getByUtenteEApp(input.getCodiceUtente(), input.getIdApplicazione());

        // Cerco i nuovi ruoli da assegnare
        List<String> ruoliDaAssegnare = input.getRuoliAssegnabili()
                .stream()
                // Prendo solo i ruoli che sono in stato di assegnato
                .filter(AssegnazioneRuoliDto.RuoliAssegnabiliDto::isAssegnato)
                // Prendo solo i nomi dei ruoli
                .map(AssegnazioneRuoliDto.RuoliAssegnabiliDto::getNomeRuolo)
                // Tengo solo quelli che non sono nella lista dei ruoli già assegnati sul DB
                .filter(el -> !getNomiRuoli(ruoliAssegnatiSuDb).contains(el))
                .toList();

        // Se non ci sono ruoli associati associo anche il master
        Groups ruoloMaster = groupsRepository.findMasterByApplicazione(input.getIdApplicazione());
        if(groupMembersRepository.getByUtenteAndRuolo(input.getCodiceUtente(), ruoloMaster.getNome(), ruoloMaster.getApp()) == null){
            groupMembersRepository.save(new GroupMembers(input.getCodiceUtente(), ruoloMaster.getNome(), utenteOperatore, dataOperazione, ufficioOperatore, ruoloMaster.getApp()));
            oimClient.associazioneRuolo(input.getCodiceUtente(), ruoloMaster.getNome());
        }

        // Assegno i nuovi ruoli
        ruoliDaAssegnare.forEach(el -> {

            // Associo i ruoli dipendenti se non sono già associati
            List<GroupsAggregazione> ruoliDipendenti = groupsAggregazioneRepository.getAggregazioneAndRuoloMasterByPrincipale(input.getIdApplicazione(), el);
            ruoliDipendenti.forEach(dip -> {
                if(groupMembersRepository.getByUtenteAndRuolo(input.getCodiceUtente(), dip.getRuoloDipendente(), dip.getIdAppDipendente()) == null){
                    groupMembersRepository.save(new GroupMembers(input.getCodiceUtente(), dip.getRuoloDipendente(), utenteOperatore, dataOperazione, ufficioOperatore, dip.getIdAppDipendente()));
                    oimClient.associazioneRuolo(input.getCodiceUtente(), dip.getRuoloDipendente());
                }
            });

            groupMembersRepository.save(new GroupMembers(input.getCodiceUtente(), el, utenteOperatore, dataOperazione, ufficioOperatore, input.getIdApplicazione()));
            oimClient.associazioneRuolo(input.getCodiceUtente(), el);
        });

        // Cerco i ruoli che sono stati rimossi
        List<String> ruoliRimossi = input.getRuoliAssegnabili()
                .stream()
                // Prendo solo i ruoli che sono in stato di non assegnato
                .filter(el -> !el.isAssegnato())
                // Prendo solo i nomi dei ruoli
                .map(AssegnazioneRuoliDto.RuoliAssegnabiliDto::getNomeRuolo)
                .filter(el -> getNomiRuoli(ruoliAssegnatiSuDb).contains(el))
                .toList();

        // Rimuovo i ruoli all'utente
        ruoliAssegnatiSuDb.stream()
                .filter(el -> ruoliRimossi.contains(el.getNomeRuolo()))
                .toList()
                .forEach(el -> {

                    // Rimozione dei dipendenti che non hanno altri padri associati
                    disassociazioneRuoliDipendenti(utenteOperatore, ufficioOperatore, dataOperazione, input.getCodiceUtente(), el.getNomeRuolo(), el.getAppId());

                    el.setUtenteCancellazione(utenteOperatore);
                    el.setUfficioCancellazione(ufficioOperatore);
                    el.setDataCancellazione(dataOperazione);
                    groupMembersRepository.save(el);

                    oimClient.rimozioneRuolo(input.getCodiceUtente(), el.getNomeRuolo());
                });

        // Rimozione del master se era l'ultimo associato
        if(groupMembersRepository.getRuoliNonMasterByAppAndUtente(input.getIdApplicazione(), input.getCodiceUtente()).size() == 0){
            groupMembersRepository.deleteMasterByAppAndUtente(utenteOperatore, ufficioOperatore, dataOperazione, input.getCodiceUtente(), input.getIdApplicazione());
            oimClient.rimozioneRuolo(input.getCodiceUtente(), ruoloMaster.getNome());
        }

        // Cerco le nuove motivazioni da assegnare
        List<Integer> motivazioniDaAssegnare = input.getMotivazioniAssegnabili()
                .stream()
                // Prendo solo le motivazioni che sono in stato di assegnato
                .filter(AssegnazioneRuoliDto.MotivazioniAssegnabiliDto::isAssegnato)
                // Prendo solo gli id delle motivazioni
                .map(AssegnazioneRuoliDto.MotivazioniAssegnabiliDto::getIdTipoMotivazione)
                // Tengo solo quelli che non sono nella lista delle motivazioni già assegnate sul DB
                .filter(el -> !getIdTipiMotivazione(applicMotivMembersSuDB).contains(el))
                .toList();

        // Assegno le nuove motivazioni
        motivazioniDaAssegnare.forEach(el ->
            applicMotivMembersRepository.save(new ApplicMotivMembers(input.getCodiceUtente(), input.getIdApplicazione(), el , utenteOperatore, ufficioOperatore, dataOperazione))
        );

        // Cerco le motivazioni che sono state rimosse
        List<Integer> motivazioniRimosse = input.getMotivazioniAssegnabili()
                .stream()
                // Prendo solo le motivazioni che sono in stato di non assegnato
                .filter(el -> !el.isAssegnato())
                // Prendo solo i nomi delle motivazioni
                .map(AssegnazioneRuoliDto.MotivazioniAssegnabiliDto::getIdTipoMotivazione)
                .filter(el -> getIdTipiMotivazione(applicMotivMembersSuDB).contains(el))
                .toList();

        // Rimuovo le motivazioni all'utente
        applicMotivMembersSuDB.stream()
                .filter(el -> motivazioniRimosse.contains(el.getIdTipoMotivazione()))
                .toList()
                .forEach(el -> {
                    el.setUtenteCancellazione(utenteOperatore);
                    el.setUfficioCancellazione(ufficioOperatore);
                    el.setDataCancellazione(dataOperazione);
                    applicMotivMembersRepository.save(el);
                });

        return input;
    }

    private List<String> getNomiRuoli(List<GroupMembers> input){
        return input.stream()
                .map(GroupMembers::getNomeRuolo)
                .toList();
    }

    private List<Integer> getIdTipiMotivazione(List<ApplicMotivMembers> input){
        return input.stream()
                .map(ApplicMotivMembers::getIdTipoMotivazione)
                .toList();
    }

    private void disassociazioneRuoliDipendenti(String utenteOperatore, String ufficioOperatore, Timestamp dataOperazione, String codiceUtente, String nomeRuolo, String idApp){

        groupsAggregazioneRepository.getAggregazioneByPrincipale(idApp, nomeRuolo)
                .forEach(el -> {
                    List<GroupMembers> ruoliAssociati = groupMembersRepository.getRuoliPrincipaliAssociatiByRuoloDipendente(nomeRuolo, idApp, el.getRuoloDipendente(), el.getIdAppDipendente(), codiceUtente);
                    if(ruoliAssociati.isEmpty()){
                        // SI PUO' ELIMINARE IL RUOLO PERCHE' NON HA ALTRI PADRI ASSOCIATI
                        groupMembersRepository.deleteByRuoloAndUtente(utenteOperatore, ufficioOperatore, dataOperazione, codiceUtente, el.getRuoloDipendente(), el.getIdAppDipendente());
                        oimClient.rimozioneRuolo(codiceUtente, el.getRuoloDipendente());
                    }
                    // ALTRIMENTI NON SI PUO' ELIMINARE IL RUOLO PERCHE' HA ALTRI PADRI ASSOCIATI
                });
    }
}

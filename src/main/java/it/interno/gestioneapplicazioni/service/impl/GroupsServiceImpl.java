package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.client.OimClient;
import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.dto.RuoliPerRegoleSicurezzaDto;
import it.interno.gestioneapplicazioni.dto.filter.ApplicazioneFilterDto;
import it.interno.gestioneapplicazioni.dto.oim.RuoloOimDto;
import it.interno.gestioneapplicazioni.entity.GroupMembers;
import it.interno.gestioneapplicazioni.entity.Groups;
import it.interno.gestioneapplicazioni.entity.GroupsAggregazione;
import it.interno.gestioneapplicazioni.entity.Gruppo;
import it.interno.gestioneapplicazioni.exception.EmptyListException;
import it.interno.gestioneapplicazioni.exception.ExceededLimitGroupMemberException;
import it.interno.gestioneapplicazioni.exception.GroupsDuplicatedException;
import it.interno.gestioneapplicazioni.mapper.GroupsMapper;
import it.interno.gestioneapplicazioni.repository.*;
import it.interno.gestioneapplicazioni.service.ApplicazioneMotivMembersService;
import it.interno.gestioneapplicazioni.service.AssegnazioneRuoliService;
import it.interno.gestioneapplicazioni.service.GroupsService;
import it.interno.gestioneapplicazioni.service.RegolaSicurezzaService;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupsServiceImpl implements GroupsService {

    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private GruppoRepository gruppoRepository;
    @Autowired
    private RegolaSicurezzaRepository regolaSicurezzaRepository;
    @Autowired
    private ApplicazioneRepository applicazioneRepository;
    @Autowired
    private OimClient oimClient;

    @Autowired
    private AssegnazioneRuoliService assegnazioneRuoliService;

    @Autowired
    private RegolaSicurezzaService regolaSicurezzaService;

    @Autowired
    private GroupsAggregazioneRepository groupsAggregazioneRepository;

    @Autowired
    private RuoloQualificaAssegnabilitaRepository ruoloQualificaAssegnabilitaRepository;

    @Autowired
    private Executor asyncTaskExecutor;

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private ApplicazioneMotivMembersService applicazioneMotivMembersService;

    @Value("${limit.remove.group.members}")
    Integer limitRemoveGroupMembers;

    @Override
    public List<GroupsDto> findAllByAppId(String idApp) {
        return groupsRepository.findAllByAppId(idApp)
                .stream()
                .map(GroupsMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<GroupsDto> salvataggio(List<GroupsDto> input, String utente, String ufficio, Timestamp data) {

        // Se la lista è vuota ritorno un errore
         if(input.isEmpty())
            throw new EmptyListException();

        // Controllo che non siano stati inseriti ruoli duplicati
        if(presenzaRuoliDuplicati(input))
            throw new GroupsDuplicatedException("Sono presenti dei ruoli duplicati");

        // Prendo i ruoli dal db
        List<GroupsDto> ruoliApplicativiDaDB = this.findAllByAppId(input.get(0).getApp());

        // Elimino dalla lista in input quelli che fanno scopa con quelli già sul db, ottenendo ruoli nuovi/modificati
        List<GroupsDto> cambiamentiDaSalvare = new ArrayList<>(input);
        cambiamentiDaSalvare.removeAll(ruoliApplicativiDaDB);

        // Elimino dalla lista dei ruoli presenti sul db quelli che in input hanno la stessa chiave,
        // ottenendo quelli che vanno eliminati
        List<GroupsDto> ruoliDaEliminare = ruoliApplicativiDaDB.stream().filter(daDB ->
                input.stream().noneMatch(el -> daDB.equals(el.getNome(), el.getApp()))
        ).toList();

        // Elimino dalla lista dei ruoli nuovi/modificati quelli la cui chiave non fa scopa con una già presente nel db,
        // ottenendo i ruoli già presenti ma che vanno modificati
        List<GroupsDto> ruoliDaAggiornare = cambiamentiDaSalvare
                .stream()
                .filter(el ->
                        ruoliApplicativiDaDB.stream().anyMatch(daDB -> el.equals(daDB.getNome(), daDB.getApp()))
                )
                .toList();

        // Elimino dalla lista dei ruoli nuovi/modificati quelli da modificare ottenendo quelli da inserire
        List<GroupsDto> ruoliDaInserire = new ArrayList<>(cambiamentiDaSalvare);
        ruoliDaInserire.removeAll(ruoliDaAggiornare);

        List<Gruppo> gruppi = gruppoRepository.findAll();

        // SALVATAGGIO
        this.inserimento(utente, ufficio, data, gruppi, ruoliDaInserire.toArray(new GroupsDto[0]));
        this.aggiornamento(utente, ufficio, data, gruppi, ruoliDaAggiornare.toArray(new GroupsDto[0]));
        this.cancellazione(utente, ufficio, data, gruppi, ruoliDaEliminare.toArray(new GroupsDto[0]));

        return this.findAllByAppId(input.get(0).getApp());
    }

    // Metodo utilizzato per eliminare tutti i ruoli applicativi quando un'applicazione viene cancellata
    @Override
    @Transactional
    public void deleteAllByApp(String appId, String utente, String ufficio, Timestamp data) {

        List<Groups> ruoliDaDb = groupsRepository.findAllByAppId(appId);
        if(ruoliDaDb!=null && ruoliDaDb.size()>limitRemoveGroupMembers){
            throw new ExceededLimitGroupMemberException("Cancellazione massiva dei ruoli applicativi non possibile,superato limite di " + limitRemoveGroupMembers + "elementi");
        }

        List<Groups> ruoliDaEliminareOim = new ArrayList<>();

        // Cancellazione
        ruoliDaDb.forEach(el -> {
            el.setUtenteCancellazione(utente);
            el.setUfficioCancellazione(ufficio);
            el.setDataCancellazione(data);
            //String ruoloOim = oimClient.ricercaRuoloId(el.getNome());
            //if(ruoloOim!=null && !ruoloOim.isEmpty()){
                ruoliDaEliminareOim.add(el);
            //}
             //CANCELLA RUOLO QUALIFICA DEL RUOLO LAVORATO
            ruoloQualificaAssegnabilitaRepository.deleteByName(el.getNome().trim());
            //CANCELLAZIONE LOGICA GROUPS_AGGREG
            List<GroupsAggregazione> groupsAggreg = groupsAggregazioneRepository.getAggregazioneByPrincipaleOrSecondaria(el.getNome().trim());
            groupsAggreg.stream().forEach(groupsAggregazione -> {
                groupsAggregazione.setUtenteCancellazione(utente);
                groupsAggregazione.setUfficioCancellazione(ufficio);
                groupsAggregazione.setDataCancellazione(data);
            });
            groupsAggregazioneRepository.saveAll(groupsAggreg);
        });
        groupsRepository.saveAll(ruoliDaDb);

        //asyncTaskExecutor.execute(() -> {
            oimClient.deleteRuoli(ruoliDaEliminareOim.stream().map(Groups::getNome).toList());
        //});

    }

    @Override
    public List<RuoliPerRegoleSicurezzaDto> findAllByAutocomplete(String parametroRicerca, List<String> ruoliDaEscludere) {

        if(ruoliDaEscludere.isEmpty())
            ruoliDaEscludere.add(" ");

        return groupsRepository.findAllByAutocomplete(StringUtils.isBlank(
                parametroRicerca) ? "" : parametroRicerca,
                ruoliDaEscludere
                )
                .stream()
                .map(el -> new RuoliPerRegoleSicurezzaDto(
                        applicazioneRepository.findAppNameByAppId(el.getApp()),
                        el.getApp(),
                        el.getNome(),
                        !regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppId(el.getNome(), el.getApp()).isEmpty()
                )).toList();
    }

    @Override
    public void updateGroupNameAfterApplicazioneNameUpdate(String appNameOld, String appNameNew, String appId, String utente, String ufficio, Timestamp data) {

        String nomeRuoloVecchio = appId + "_G_" + appNameOld.replace(" ", "_").toUpperCase();
        String nomeRuoloNuovo = appId + "_G_" + appNameNew.replace(" ", "_").toUpperCase();

        // Converto eventuali caratteri speciali
        nomeRuoloVecchio = ConversionUtils.conversioneCaratteriSpeciali(nomeRuoloVecchio);
        nomeRuoloNuovo = ConversionUtils.conversioneCaratteriSpeciali(nomeRuoloNuovo);

        Groups ruoloMaster = groupsRepository.findByAppIdAndName(appId, nomeRuoloVecchio);

        if(ruoloMaster == null)
            return;

        groupsRepository.updateMaster(nomeRuoloVecchio, nomeRuoloNuovo, utente, ufficio, data);
        oimClient.renameRuolo(nomeRuoloVecchio, nomeRuoloNuovo);
    }

    // Metodo per l'inserimento
    private List<GroupsDto> inserimento(String utente, String ufficio, Timestamp data, List<Gruppo> gruppi, GroupsDto... ruoliApplicativi){
        List<Groups> ruoliEntity = Arrays.asList(ruoliApplicativi)
                .stream()
                .map(el -> {
                    el.setUtenteInserimento(utente);
                    el.setUfficioInserimento(ufficio);
                    el.setDataInserimento(data);
                    return GroupsMapper.toEntity(el, gruppi);
                })
                .toList();

            // Controllo univocità nome ruolo
        ruoliEntity.forEach(el -> {
            if(!groupsRepository.findByNomeRuolo(el.getNome()).isEmpty())
                throw new GroupsDuplicatedException("Il ruolo " + el.getNome() + " esiste già in un'altra applicazione");
        });






        List<GroupsDto> results = groupsRepository.saveAll(ruoliEntity).stream().map(GroupsMapper::toDto).toList();

        oimClient.creazioneRuoli(results.stream().map(el -> new RuoloOimDto(
                el.getNome(),
                el.getDescrizione()
        )).toList());

        return results;
    }

    // Metodo per l'aggiornamento
    private List<GroupsDto> aggiornamento(String utente, String ufficio, Timestamp data, List<Gruppo> gruppi, GroupsDto... ruoliApplicativi){

        List<Groups> ruoliEntity = Arrays.asList(ruoliApplicativi)
                .stream()
                .map(el -> {
                    el.setUtenteAggiornamento(utente);
                    el.setUfficioAggiornamento(ufficio);
                    el.setDataAggiornamento(data);
                    return GroupsMapper.toEntity(el, gruppi);
                })
                .toList();

        List<GroupsDto> results = groupsRepository.saveAll(ruoliEntity).stream().map(GroupsMapper::toDto).toList();

        oimClient.modificaRuoli(results.stream().map(el -> new RuoloOimDto(
                el.getNome(),
                el.getDescrizione()
        )).toList());

        return results;
    }

    // Metodo per la cancellazione

    private void cancellazione(String utente, String ufficio, Timestamp data, List<Gruppo> gruppi, GroupsDto... ruoliApplicativi){

       List<Groups> ruoliEntity = Arrays.asList(ruoliApplicativi)
                .stream()
                .map(el -> {
                    el.setUtenteCancellazione(utente);
                    el.setUfficioCancellazione(ufficio);
                    el.setDataCancellazione(data);
                    el.setQualificheAssegnate(new ArrayList<>());
                    return GroupsMapper.toEntity(el, gruppi);
                })
                .toList();
        groupsRepository.saveAll(ruoliEntity);

        ruoliEntity.stream().forEach(ruolo->{
            assegnazioneRuoliService.deleteAll(ruolo.getNome(),ruolo.getApp(),utente,ufficio,data);
            regolaSicurezzaService.deleteAll(ruolo.getNome(),ruolo.getApp(),utente,ufficio,data);
        });

        if(!CollectionUtils.isEmpty(ruoliEntity)) {
            String[] listaRuoli = ruoliEntity.stream().map(Groups::getGruppo).toArray(String[]::new);
            String idApplicazione = ruoliEntity.stream().findFirst().get().getApp();

            List<String> utenti = groupMembersRepository.getUtentiDistintiByApp(idApplicazione);
            utenti.stream().forEach(utenteApp->{
                List<GroupMembers> groupMembers = groupMembersRepository.getByUtenteAndAppNotInCodiceRuolo(utenteApp, idApplicazione, listaRuoli);
                if(groupMembers.size()==1){
                    log.debug("esiste solo il ruolo master lo cancello utente {} , idApp {} codiceRuolo {} ", utenteApp, idApplicazione, listaRuoli);

                    applicazioneMotivMembersService.deleteByAppIdAndUtente(idApplicazione, utenteApp, utente, ufficio, data);

                }else{
                    log.debug("esistono altri ruoli associati non cancello il master utente {} , idApp {} codiceRuolo {} ", utenteApp, idApplicazione, listaRuoli);
                }
            });
        }

        oimClient.deleteRuoli(ruoliEntity.stream().map(Groups::getNome).toList());
    }

    private boolean presenzaRuoliDuplicati(List<GroupsDto> ruoliApplicativi){

        List<GroupsDto> copia = new ArrayList<>(ruoliApplicativi);

        for(int i = 0; i < ruoliApplicativi.size(); i++){

            // Prendo il primo elemento e verifico se ne trovo un altro con chiave uguale nella restante lista
            GroupsDto temp = copia.remove(0);
            if(copia.stream().anyMatch(el -> el.getNome().trim().equals(temp.getNome().trim()) && el.getApp().equals(temp.getApp())))
                return true;
        }

        return false;
    }

    @Override
    public Page<RuoliPerRegoleSicurezzaDto> searchAndPaginateRegoleSicurezza(int flagRicerca, String parametroRicerca, ApplicazioneFilterDto filtro) {

        Pageable pageable = PageRequest.of(
                filtro.getPagina(),
                filtro.getNumeroElementi(),
                Sort.by(
                        new Sort.Order(Sort.Direction.fromString(filtro.getSortDirection()), filtro.getSortBy()).ignoreCase()
                )
        );

        Page<Groups> ruoli = groupsRepository.searchAndPaginateRegoleSicurezza(flagRicerca, parametroRicerca.toUpperCase(), pageable);
        List<RuoliPerRegoleSicurezzaDto> result = new ArrayList<>();

        ruoli.forEach(el -> {
            RuoliPerRegoleSicurezzaDto temp = new RuoliPerRegoleSicurezzaDto();
            temp.setNomeRuolo(el.getNome());
            temp.setAppId(el.getApp());
            temp.setNomeApplicazione(applicazioneRepository.findAppNameByAppId(el.getApp()));
            temp.setRegoleSicurezza(!regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppId(el.getNome(), el.getApp()).isEmpty());
            result.add(temp);
        });

        return new PageImpl<>(result, pageable, ruoli.getTotalElements());
    }
}
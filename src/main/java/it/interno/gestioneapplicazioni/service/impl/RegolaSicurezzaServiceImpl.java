package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.client.OimClient;
import it.interno.gestioneapplicazioni.dto.*;
import it.interno.gestioneapplicazioni.entity.*;
import it.interno.gestioneapplicazioni.exception.ExceededLimitGroupMemberException;
import it.interno.gestioneapplicazioni.exception.RegolaDuplicatedException;
import it.interno.gestioneapplicazioni.mapper.RegolaSicurezzaMapper;
import it.interno.gestioneapplicazioni.repository.*;
import it.interno.gestioneapplicazioni.service.ApplicazioneMotivMembersService;
import it.interno.gestioneapplicazioni.service.RegolaSicurezzaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class RegolaSicurezzaServiceImpl implements RegolaSicurezzaService {

    @Autowired
    private RegolaSicurezzaRepository regolaSicurezzaRepository;
    @Autowired
    private RegolaSicurezzaVistaRepository regolaSicurezzaVistaRepository;
    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private OimClient oimClient;

    @Autowired
    private ApplicazioneMotivMembersService applicazioneMotivMembersService;

    @Value("${limit.remove.group.members}")
    Integer limitRemoveGroupMembers;

    @Override
    public List<RegolaSicurezzaDto> getAllRegoleByRuolo(String nomeRuolo, String appId) {

        List<RegolaSicurezzaVista> regoleSicurezza = regolaSicurezzaVistaRepository.getRegoleByNomeRuoloAndAppId(nomeRuolo, appId);

        // Raggruppo i record per id regola, per poi creare le singole regole di sicurezza
        Map<Integer, List<RegolaSicurezzaVista>> mappaRegole = new HashMap<>();
        regoleSicurezza.forEach(el -> {

            if(!mappaRegole.containsKey(el.getIdRegola()))
                mappaRegole.put(el.getIdRegola(), new ArrayList<>());

            mappaRegole.get(el.getIdRegola()).add(el);
        });

        // Ciclo la mappa per creare le N regole del ruolo
        List<RegolaSicurezzaDto> result = new ArrayList<>();

        for(Map.Entry<Integer, List<RegolaSicurezzaVista>> el : mappaRegole.entrySet()){
            result.add(RegolaSicurezzaMapper.toDto(el.getValue()));
        }

        return result;
    }

    @Override
    public RegolaSicurezzaDto getRegolaByRuoloAndIdRegola(String nomeRuolo, String appId, Integer idRegola) {
        return RegolaSicurezzaMapper.toDto(regolaSicurezzaVistaRepository.getRegoleByNomeRuoloAndAppIdAndIdRegola(nomeRuolo, appId, idRegola));
    }

    @Override
    @Transactional
    public List<RegolaSicurezzaDto> salvataggio(List<RegolaSicurezzaDto> input, String utente, String ufficio, Timestamp data) {

        if(input.isEmpty())
            return new ArrayList<>();

        presenzaRegoleDuplicate(input);

        String nomeRuolo = input.get(0).getRuolo();
        String appId = input.get(0).getAppId();

        List<RegolaSicurezzaDto> inDb = this.getAllRegoleByRuolo(nomeRuolo, appId);

        // Le regole passate in input con numero regola a NULL sono nuove e vanno inserite
        List<RegolaSicurezzaDto> daInserire = input.stream().filter(el -> el.getNumeroRegola() == null).toList();
        // Le regole passate in input con numero regola diverso da NULL sono già presenti e vanno aggiornate
        List<RegolaSicurezzaDto> daAggiornare = input.stream().filter(el -> el.getNumeroRegola() != null && el.isEdit()).toList();

        // Lascio tra quelle che mi vengono passate in input solo quelle con id regola valorizzato, quindi sono già presenti in DB
        input = input.stream().filter(el -> el.getNumeroRegola() != null).toList();
        // Elimino dalle regole presenti in DB quelle passate in input, ottenendo quelle che sono state eliminate
        List<RegolaSicurezzaDto> daRimuovere = new ArrayList<>(inDb);
        daRimuovere.removeAll(input);

        daRimuovere.forEach(el -> this.delete(el.getRuolo(), el.getAppId(), el.getNumeroRegola(), utente, ufficio, data));
        daInserire.forEach(el -> this.insert(el, utente, ufficio, data));
        daAggiornare.forEach(el -> this.update(el, utente, ufficio, data));

        return this.getAllRegoleByRuolo(nomeRuolo, appId);
    }

    private void insert(RegolaSicurezzaDto input, String utente, String ufficio, Timestamp data) {

        Integer idRegola = regolaSicurezzaRepository.getNextIdRegolaByNomeRuolo(input.getRuolo(), input.getAppId());

        List<RegolaSicurezza> regolaSicurezza = RegolaSicurezzaMapper.toEntity(input);
        regolaSicurezza.forEach(el -> {
            el.setNumeroRegola(idRegola);
            el.setUtenteInserimento(utente);
            el.setUfficioInserimento(ufficio);
            el.setDataInserimento(data);
        });
        regolaSicurezzaRepository.saveAll(regolaSicurezza);
    }

    private void update(RegolaSicurezzaDto input, String utente, String ufficio, Timestamp data) {

        // Chiudo la regola vecchia
        List<RegolaSicurezza> daChiudere = regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppIdAndIdRegola(input.getRuolo(), input.getAppId(), input.getNumeroRegola());
        daChiudere.forEach(el -> {
            el.setUtenteCancellazione(utente);
            el.setUfficioCancellazione(ufficio);
            el.setDataCancellazione(data);
        });

        regolaSicurezzaRepository.deleteAll(daChiudere);

        // Salvo la regola nuova
        List<RegolaSicurezza> regolaSicurezza = RegolaSicurezzaMapper.toEntity(input);
        regolaSicurezza.forEach(el -> {
            el.setUtenteInserimento(utente);
            el.setUfficioInserimento(ufficio);
            el.setDataInserimento(data);
        });
        regolaSicurezzaRepository.saveAll(regolaSicurezza);

        this.checkValiditaRegole(input.getRuolo(), input.getAppId(), utente, ufficio, data);
    }

    private void delete(String nomeRuolo, String idApplicazione, Integer idRegola, String utente, String ufficio, Timestamp data) {

        List<RegolaSicurezza> daChiudere = regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppIdAndIdRegola(nomeRuolo, idApplicazione, idRegola);
        /*daChiudere.forEach(el -> {
            el.setUtenteCancellazione(utente);
            el.setUfficioCancellazione(ufficio);
            el.setDataCancellazione(data);
        });*/

        regolaSicurezzaRepository.deleteAll(daChiudere);
        this.checkValiditaRegole(nomeRuolo, idApplicazione, utente, ufficio, data);
    }

    @Override
    public void deleteAll(String nomeRuolo, String idApplicazione, String utente, String ufficio, Timestamp data) {
        List<RegolaSicurezza> daChiudere = regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppId(nomeRuolo, idApplicazione);

       /* daChiudere.forEach(el -> {
            el.setUtenteCancellazione(utente);
            el.setUfficioCancellazione(ufficio);
            el.setDataCancellazione(data);
         });*/

        regolaSicurezzaRepository.deleteAll(daChiudere);
        this.checkValiditaRegole(nomeRuolo, idApplicazione, utente, ufficio, data);
        List<GroupMembers> associazioni = groupMembersRepository.getByIdApplicazione(idApplicazione);
        if(associazioni.size()==1){
            GroupMembers groupMember =  associazioni.getFirst();
            if(regolaSicurezzaRepository.isRuoloApplicativoAssegnabile(groupMember.getNomeUtente(), idApplicazione, groupMember.getNomeRuolo()) == 0){
                groupMember.setUtenteCancellazione(utente);
                groupMember.setUfficioCancellazione(ufficio);
                groupMember.setDataCancellazione(data);
                groupMembersRepository.save(groupMember);
                oimClient.rimozioneRuolo(utente,groupMember.getNomeRuolo());
            }
        }
     }


    @Override
    public void deleteAll(String idApplicazione, String utente, String ufficio, Timestamp data) {
        List<RegolaSicurezza> daChiudere = regolaSicurezzaRepository.getRegoleByAppId(idApplicazione);

        /*daChiudere.forEach(el -> {
            el.setUtenteCancellazione(utente);
            el.setUfficioCancellazione(ufficio);
            el.setDataCancellazione(data);
        });*/

        regolaSicurezzaRepository.deleteAll(daChiudere);
    }

    private void checkValiditaRegole(String codiceRuolo, String idApplicazione, String utente, String ufficio, Timestamp data){

        List<GroupMembers> associazioni = groupMembersRepository.getByRuolo(codiceRuolo, idApplicazione);
        List<GroupMembers> daRimuovere = new ArrayList<>();


  //step stepGroupMemberGetByRuolo
        associazioni.forEach(el -> {

            if(regolaSicurezzaRepository.isRuoloApplicativoAssegnabile(el.getNomeUtente(), idApplicazione, codiceRuolo) == 0){
                // SI DEVE DISASSOCIARE IL RUOLO
                el.setUtenteCancellazione(utente);
                el.setUfficioCancellazione(ufficio);
                el.setDataCancellazione(data);
                daRimuovere.add(el);
                //oimClient.rimozioneRuolo(el.getNomeUtente(),el.getNomeRuolo());
            }
        });
        List<String> nomiutente =  daRimuovere.stream().map(GroupMembers::getNomeUtente).toList();
       //CHIAMATA MASSIVA
        oimClient.rimozioneRuoloAUtenti(codiceRuolo,nomiutente);

        groupMembersRepository.saveAll(daRimuovere);
        //recupero gli utenti associati appid,per ogni utente
        //quanti ruoli esistono per appid e utente
        List<String> utenti = groupMembersRepository.getUtentiDistintiByApp(idApplicazione);
        utenti.stream().forEach(utenteApp->{
            List<GroupMembers> groupMembers = groupMembersRepository.getByUtenteAndAppNotInCodiceRuolo(utenteApp,idApplicazione,codiceRuolo);
            if(groupMembers.size()==1){
                log.debug("esiste solo il ruolo master lo cancello utente {} , idApp {} codiceRuolo {} " , utenteApp,idApplicazione,codiceRuolo);
                GroupMembers master =  groupMembers.getFirst();
                master.setUtenteCancellazione(utente);
                master.setUfficioCancellazione(ufficio);
                master.setDataCancellazione(data);
                oimClient.rimozioneRuolo(master.getNomeUtente(),master.getNomeRuolo());
                groupMembersRepository.save(master);

                applicazioneMotivMembersService.deleteByAppIdAndUtente(idApplicazione, utenteApp, utente, ufficio, data);

            }else{
                log.debug("esistono altri ruoli associati non cancello il master utente {} , idApp {} codiceRuolo {} " , utenteApp,idApplicazione,codiceRuolo);
            }
        });
    }

    @Override
    @Transactional
    public void duplicazioneRegole(DuplicazioneRegoleDto input, String utente, String ufficio, Timestamp data) {

        List<RegolaSicurezza> daCopiare = regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppId(input.getRuoloSorgente().getNome(), input.getRuoloSorgente().getApp());
        List<RegolaSicurezza> nuoveRegole = new ArrayList<>();

        input.getRuoliDestinazione().forEach(ruolo -> {
            Integer numeroRegola = regolaSicurezzaRepository.getNextIdRegolaByNomeRuolo(ruolo.getNome(), ruolo.getApp());
            List<String> nomiRegoleRuolo = regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppId(ruolo.getNome(), ruolo.getApp()).stream().map(RegolaSicurezza::getNomeRegola).toList();

            daCopiare.forEach(regola -> {

                if(nomiRegoleRuolo.contains(regola.getNomeRegola()))
                    throw new RegolaDuplicatedException("Il nome regola " + regola.getNomeRegola() + " è duplicato nell'applicazione " + regola.getAppId());

                nuoveRegole.add(new RegolaSicurezza(
                        ruolo.getNome(),
                        ruolo.getApp(),
                        numeroRegola + regola.getNumeroRegola(),
                        regola.getProgressivoRegola(),
                        regola.getIdGruppoLavoro(),
                        regola.getIdEnte(),
                        regola.getRegione(),
                        regola.getProvincia(),
                        regola.getComune(),
                        regola.getCodiceUfficio(),
                        regola.getIdFunzione(),
                        regola.getRuoloQualifica(),
                        regola.getNomeRegola(),
                        regola.getTipoRegola(),
                        utente,
                        ufficio,
                        data
                ));
            });
        });

        regolaSicurezzaRepository.saveAll(nuoveRegole);
    }

    private void presenzaRegoleDuplicate(List<RegolaSicurezzaDto> regoleSicurezza){

        List<RegolaSicurezzaDto> copia = new ArrayList<>(regoleSicurezza);

        for(int i = 0; i < regoleSicurezza.size(); i++){

            // Prendo il primo elemento e verifico se ne trovo un altro con chiave uguale nella restante lista
            RegolaSicurezzaDto temp = copia.remove(0);
            if(copia.stream().anyMatch(el -> el.getNomeRegola().trim().equals(temp.getNomeRegola().trim())))
                throw new RegolaDuplicatedException("Il nome regola " + temp.getNomeRegola() + " è duplicato");
        }
    }
}

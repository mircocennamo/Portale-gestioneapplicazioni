package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.client.OimClient;
import it.interno.gestioneapplicazioni.dto.ApplicazioneDto;
import it.interno.gestioneapplicazioni.dto.filter.ApplicazioneFilterDto;
import it.interno.gestioneapplicazioni.entity.*;
import it.interno.gestioneapplicazioni.exception.ApplicazioneNonAggiornabileException;
import it.interno.gestioneapplicazioni.exception.ApplicazioneNotFoundException;
import it.interno.gestioneapplicazioni.exception.EmptyListException;
import it.interno.gestioneapplicazioni.mapper.ApplicazioneMapper;
import it.interno.gestioneapplicazioni.repository.*;
import it.interno.gestioneapplicazioni.service.*;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.interno.gestioneapplicazioni.repository.specification.ApplicazioneSpecification.*;

@Service
@Validated
public class ApplicazioneServiceImpl implements ApplicazioneService {

    @Autowired
    private ApplicazioneRepository applicazioneRepository;
    @Autowired
    private ApplicazioneMapper applicazioneMapper;
    @Autowired
    private AmbitoRepository ambitoRepository;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private AssegnazioneRuoliService assegnazioneRuoliService;

    @Autowired
    private RegolaSicurezzaService regolaSicurezzaService;

    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private RegolaSicurezzaRepository regolaSicurezzaRepository;
    @Autowired
    private ApplicMotivMembersRepository applicMotivMembersRepository;
    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private OimClient oimClient;

    @Autowired
    private ApplicazioneMotivazioneService applicazioneMotivazioneService;

    @Autowired
    private ApplicazioneMotivazioneRepository applicazioneMotivazioneRepository;

    @Autowired
    private ApplicazioneMotivMembersService applicazioneMotivMembersService;



    @Override
    public ApplicazioneDto getById(String idApplicazione) {

        ApplicazioneDto applicazione = applicazioneMapper.toDto(
                applicazioneRepository.findById(idApplicazione).orElse(null)
        );

        if(applicazione == null)
            throw new ApplicazioneNotFoundException("L'applicazione non è presente in base dati");

        // Prendo la lista di motivazioni dal db e setto la lista degli id nel DTO
        List<Integer> idMotivazioni = applicazioneMotivazioneRepository.findByIdApp(idApplicazione)
                .stream().map(ApplicazioneMotivazione::getIdTipoMotivazione)
                .toList();

        applicazione.setIdMotivazione(idMotivazioni);
        applicazione.setRuoliApplicativi(groupsService.findAllByAppId(idApplicazione));

        return applicazione;
    }

    @Override
    public ApplicazioneDto getByNome(String nome) {
        ApplicazioneDto applicazione = applicazioneMapper.toDto(
                applicazioneRepository.findByNome(nome)
        );

        if(applicazione == null)
            throw new ApplicazioneNotFoundException("L'applicazione non è presente in base dati");

        // Prendo la lista di motivazioni dal db e setto la lista degli id nel DTO
        List<Integer> idMotivazioni = applicazioneMotivazioneRepository.findByIdApp(applicazione.getAppId())
                .stream().map(ApplicazioneMotivazione::getIdTipoMotivazione)
                .toList();

        applicazione.setIdMotivazione(idMotivazioni);
        applicazione.setRuoliApplicativi(groupsService.findAllByAppId(applicazione.getAppId()));

        return applicazione;
    }

    @Override
    public List<ApplicazioneDto> getAllApplicazioni() {
        return applicazioneRepository.getAllApplicazioni()
                .stream().map(el -> {
                    ApplicazioneDto temp = applicazioneMapper.toDto(el);
                    temp.setRuoliApplicativi(groupsService.findAllByAppId(el.getAppId()));
                    return temp;
                })
                .toList();
    }

    @Override
    public List<ApplicazioneDto> getAllApplicazioniByAmbito(Integer idAmbito) {

        if(idAmbito == null)
            return this.getAllApplicazioni();

        return applicazioneRepository.getApplicazioniByAmbito(idAmbito)
                .stream().map(el -> {
                    ApplicazioneDto temp = applicazioneMapper.toDto(el);
                    temp.setRuoliApplicativi(groupsService.findAllByAppId(el.getAppId()));
                    return temp;
                })
                .toList();
    }

    @Override
    public Page<ApplicazioneDto> searchAndPaginate(ApplicazioneFilterDto filtro) {

        // Mi calcolo il metodo di paginazione
        Pageable pageable = getPageable(
            filtro.getPagina(),
            filtro.getNumeroElementi(),
            filtro.getSortBy(),
            filtro.getSortDirection()
        );

        Page<Applicazione> pages;

        // Mi prendo i criteri nel caso in cui c'è l'ambito oppure no
        Specification<Applicazione> spec = getSpecificationRicerca(filtro);
        pages = applicazioneRepository.findAll(
                spec, pageable
        );


        List<ApplicazioneDto> dtos = pages.stream()
            .map(el -> {
                ApplicazioneDto temp = applicazioneMapper.toDto(el);
                temp.setRuoliApplicativi(groupsService.findAllByAppId(el.getAppId()));
                temp.getRuoliApplicativi().forEach(ruolo -> ruolo.setRegoleSicurezza(!regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppId(ruolo.getNome(), ruolo.getApp()).isEmpty()));
                return temp;
            })
            .toList();

        return new PageImpl<>(dtos, pageable, pages.getTotalElements());
    }

    @Override
    @Transactional
    public ApplicazioneDto insertNew(ApplicazioneDto input, String utenteInserimento, String ufficioInserimento, Timestamp data) {

        List<ApplicazioneMotivazione> motivazioni = new ArrayList<>();

        // Calcolo il massimo idCatalogo presente per ambito e gli aggiungo 1024
        double idCatalogo = applicazioneRepository.getMaxOrderIdCatalogoByAmbito(input.getIdOrdineAmbito()).orElse(0.0) + 1024;

        // Settaggio delle proprietà
        this.trimApplicazione(input);
        input.setAppScope(ambitoRepository.getAmbitoByOrderId(input.getIdOrdineAmbito()).getDescrizioneAmbito());
        input.setIdOrdineCatalogo(idCatalogo == 0.0 ? 1024.0 : idCatalogo);
        input.setAppDataIni(LocalDate.now());
        input.setUtenteInserimento(utenteInserimento);
        input.setUfficioInserimento(ufficioInserimento);
		input.setDataInserimento(data);
        input.setAppDataFin(LocalDate.of(9999, 12, 31));

        // Inserisco come id un valore di default che verrà sovrascritto dal trigger
        input.setAppId("1");

        if(input.getDataFineOperativita() == null)
            input.setDataFineOperativita(LocalDate.of(9999, 12, 31));

        // Salvataggio Applicazione e ricerca
        Applicazione applicazioneInserita = applicazioneRepository.save(applicazioneMapper.toEntity(input));
        String idApp = applicazioneRepository.findByNome(applicazioneInserita.getAppName()).getAppId();

        // Creo le motivazioni da inserire dalla lista di id passata nel dto
        input.getIdMotivazione().forEach(el ->
            motivazioni.add(
                new ApplicazioneMotivazione(
                    idApp,
                    el,
                    data,
                    utenteInserimento,
                    ufficioInserimento
                )
            )
        );

        // Salvataggio Motivazioni
        applicazioneMotivazioneRepository.saveAll(motivazioni);

        return this.getById(idApp);
    }

    @Override
    @Transactional
    public ApplicazioneDto updateApplicazione(ApplicazioneDto input, String utenteAggiornamento, String ufficioAggiornamento, Timestamp data) {

        Applicazione applicazioneSuDb = applicazioneRepository.findById(input.getAppId()).orElse(null);

        if(applicazioneSuDb == null)
            throw new ApplicazioneNotFoundException("L'applicazione " + input.getAppName() + " non è stata trovata");

        if(!Objects.equals(input.getIdOrdineAmbito(), applicazioneSuDb.getIdOrdineAmbito()) && !groupsRepository.findAllByAppId(input.getAppId()).isEmpty())
            throw new ApplicazioneNonAggiornabileException("Non si può cambiare ambito se ci sono ruoli creati.");

        this.trimApplicazione(input);
        input.setAppScope(ambitoRepository.getAmbitoByOrderId(input.getIdOrdineAmbito()).getDescrizioneAmbito());

        // Mi prendo la lista di motivazioni dell'app dal db
        List<ApplicazioneMotivazione> motivazioniDaDb = applicazioneMotivazioneRepository.findByIdApp(input.getAppId());

        // Cerco se ci sono motivazioni da cancellare e se ci sono setto per ognuna l'utente e la data cancellazione
        List<ApplicazioneMotivazione> motivazioniDaCancellare = motivazioniDaDb
            .stream().filter(el ->
                !input.getIdMotivazione().contains(el.getIdTipoMotivazione())
            ).toList()
            .stream().map(el -> {
                el.setUtentecancellazione(utenteAggiornamento);
                el.setDataCancellazione(data);
                return el;
            }).toList();

        // Cerco se ci sono motivazioni da aggiungere e se ci sono creo i relativi oggetti
        List<ApplicazioneMotivazione> motivazioniNuove = input.getIdMotivazione().stream().filter(el ->
                !motivazioniDaDb.stream().map(ApplicazioneMotivazione::getIdTipoMotivazione).toList().contains(el)
            ).toList()
            .stream().map(id -> new ApplicazioneMotivazione(input.getAppId(), id, data, utenteAggiornamento, ufficioAggiornamento))
            .toList();

        // Creo una lista comune tra le eventuali motivazioni da cancellare e quelle da aggiungere e le salvo
        List<ApplicazioneMotivazione> cambiamentiDaApportare = new ArrayList<>();
        cambiamentiDaApportare.addAll(motivazioniDaCancellare);
        cambiamentiDaApportare.addAll(motivazioniNuove);
        applicazioneMotivazioneRepository.saveAll(cambiamentiDaApportare);

        // Mi prendo la lista degli utenti collegati all'app dal db
        List<ApplicMotivMembers> applicMotivMembersSuDB = Optional.ofNullable(applicMotivMembersRepository.getByApp(input.getAppId())).orElse(new ArrayList<>());

        // Cerco le motivazioni da cancellare in base ai dati sulla tabella applicMotivMembers
        List<Integer> idMtvDaCanc = motivazioniDaCancellare.stream()
                .map(ApplicazioneMotivazione::getIdTipoMotivazione)
                .filter(mtvDaCanc -> getIdTipoMotivazioni(applicMotivMembersSuDB).contains(mtvDaCanc)).toList();

        // Rimuovo dalla tabella applicMotivMembers le motivazioni agli utenti
        applicMotivMembersSuDB.stream()
                .filter(applicMotivMembers -> idMtvDaCanc.contains(applicMotivMembers.getIdTipoMotivazione())).toList()
                .forEach(applicMotivMembers -> {
                    applicMotivMembers.setUfficioCancellazione(ufficioAggiornamento);
                    applicMotivMembers.setUtenteCancellazione(utenteAggiornamento);
                    applicMotivMembers.setDataCancellazione(data);
                    applicMotivMembersRepository.save(applicMotivMembers);
                });

        // Se motivazione nuova è "NON PREVISTO" allora associo questa motivazione a tutti gli utenti che prima avevano
        // una motivazione selezionata
        if(input.getIdMotivazione().size() == 1 && input.getIdMotivazione().get(0) == 0) {

            List<String> groupMembersList = Optional.ofNullable(groupMembersRepository.getUtentiDistintiByApp(input.getAppId())).orElse(new ArrayList<>());

            groupMembersList.stream()
                    .forEach(codiceUtente -> {
                        ApplicMotivMembers amm = new ApplicMotivMembers();
                        amm.setCodiceUtente(codiceUtente);
                        amm.setAppId(input.getAppId());
                        amm.setIdTipoMotivazione(input.getIdMotivazione().get(0));
                        amm.setDataInserimento(data);
                        amm.setUtenteInserimento(utenteAggiornamento);
                        amm.setUfficioInserimento(ufficioAggiornamento);
                        applicMotivMembersRepository.save(amm);
                    });
        }

        // Carico l'applicazione da db per vedere se è stata modificata se si la modifico, altrimenti evito
        Applicazione applicazioneInput = applicazioneMapper.toEntity(input);

        // Se l'app in ingresso non è uguale a quella su db viene aggiornata e vengono settati i campi di aggiornamento
        if(!applicazioneSuDb.equals(applicazioneInput)){
            applicazioneInput.setUtenteAggiornamento(utenteAggiornamento);
            applicazioneInput.setUfficioAggiornamento(ufficioAggiornamento);
            applicazioneInput.setDataAggiornamento(data);

            // Se cambia il nome dell'applicazione, cambio il nome del ruolo master
            if(!applicazioneSuDb.getAppName().equalsIgnoreCase(applicazioneInput.getAppName()))
                groupsService.updateGroupNameAfterApplicazioneNameUpdate(applicazioneSuDb.getAppName(),
                        applicazioneInput.getAppName(), applicazioneInput.getAppId(), utenteAggiornamento, ufficioAggiornamento, data);

            // Se è cambiata la macrocategoria, resetto il valore dell'ordinamento
            if(!Objects.equals(applicazioneSuDb.getIdOrdineAmbito(), applicazioneInput.getIdOrdineAmbito())){
                double idCatalogo = applicazioneRepository.getMaxOrderIdCatalogoByAmbito(input.getIdOrdineAmbito()).orElse(0.0) + 1024;
                applicazioneInput.setIdOrdineCatalogo(idCatalogo == 0.0 ? 1024.0 : idCatalogo);
            }

            applicazioneRepository.save(applicazioneInput);
        }

        // Mi richiamo da db l'applicazione e tutte le sue motivazioni associate
        return this.getById(input.getAppId());
    }

    private List<Integer> getIdTipoMotivazioni(List<ApplicMotivMembers> listApplMotivMembers) {
        return listApplMotivMembers.stream().map(ApplicMotivMembers::getIdTipoMotivazione).toList();
    }



    @Override
    @Transactional
    public ApplicazioneDto deleteApplicazione(String idApplicazione, String utenteCancellazione, String ufficioCancellazione, Timestamp data) {

        Applicazione applicazione = applicazioneRepository.findById(idApplicazione).orElse(null);

        if (applicazione == null)
            throw new ApplicazioneNotFoundException("L'applicazione non è stata trovata.");


        //GROUP_MEMBERS
        assegnazioneRuoliService.deleteAll(idApplicazione,utenteCancellazione,ufficioCancellazione,data);

        //SEC_REGOLE_SICUREZZA
        regolaSicurezzaService.deleteAll(idApplicazione,utenteCancellazione,ufficioCancellazione,data);

        //GROUP
        groupsService.deleteAllByApp(idApplicazione,utenteCancellazione,ufficioCancellazione,data);

        applicazioneMotivazioneService.deleteByAppId(idApplicazione,utenteCancellazione,ufficioCancellazione,data);

        applicazioneMotivMembersService.deleteByAppId(idApplicazione,utenteCancellazione,ufficioCancellazione,data);
        applicazione.setUtenteCancellazione(utenteCancellazione);
        applicazione.setUfficioCancellazione(ufficioCancellazione);
        applicazione.setDataCancellazione(data);
        applicazione.setAppDataFin(ConversionUtils.timestampToLocalDate(data));

        //SEC_APPLICAZIONE
        return applicazioneMapper.toDto(applicazioneRepository.save(applicazione));
    }

    @Override
    @Transactional
    public List<ApplicazioneDto> ordinamentoApplicazioni(List<ApplicazioneDto> applicazioni, String utente, String ufficio, Timestamp data) {

        // Se la lista è vuota ritorno un errore
        if(applicazioni.isEmpty())
            throw new EmptyListException();

        Integer idAmbito = applicazioni.get(0).getIdOrdineAmbito();

        // Recupero da DB tulle le applicazioni dello stesso ambito di quelle inserite
        List<ApplicazioneDto> applicazioniDaDb = applicazioneRepository.getApplicazioniByAmbito(applicazioni.get(0).getIdOrdineAmbito())
                .stream().map(el -> applicazioneMapper.toDto(el)).toList();

        // Rimuovo dalle applicazioni inserite quelle che non devono essere modificate
        applicazioni = applicazioni.stream().filter(el ->
                applicazioniDaDb.stream()
                        .noneMatch(daDB -> daDB.getAppId().equals(el.getAppId()) && daDB.getIdOrdineCatalogo().equals(el.getIdOrdineCatalogo()))
        ).toList();

        // Converto tutte le applicazioni passate in input in entity e valorizzo proprieta aggiornamento
        List<Applicazione> daInserire = applicazioni.stream()
            .map(el -> {
                el.setUtenteAggiornamento(utente);
                el.setUfficioAggiornamento(ufficio);
                el.setDataAggiornamento(data);
                return applicazioneMapper.toEntity(el);
            })
            .toList();

        applicazioneRepository.saveAll(daInserire);
        return this.getAllApplicazioniByAmbito(idAmbito);
    }

    private Pageable getPageable(Integer numeroPagina, Integer numeroElementi, String sortBy, String sortDirection){

        return PageRequest.of(
                numeroPagina,
                numeroElementi,
                Sort.by(
                        new Sort.Order(Sort.Direction.fromString(sortDirection), sortBy).ignoreCase()
                )
        );
    }

    private Specification<Applicazione> getSpecificationRicerca(ApplicazioneFilterDto filtro){

        if(filtro.getAmbito() == null)
            return appIdLike(filtro.getParametroRicerca())
                    .or(appScopeLike(filtro.getParametroRicerca()))
                    .or(appDescriptionLike(filtro.getParametroRicerca()))
                    .or(appNameLike(filtro.getParametroRicerca()))
                    .and(dataInizioValiditaLessThan(LocalDate.now()))
                    .and(dataFineValiditaGreaterThanOrNull(LocalDate.now()))
                    .and(dataCancellazioneIsNull());

        return appIdLike(filtro.getParametroRicerca())
                .or(appScopeLike(filtro.getParametroRicerca()))
                .or(appDescriptionLike(filtro.getParametroRicerca()))
                .or(appNameLike(filtro.getParametroRicerca()))
                .and(dataInizioValiditaLessThan(LocalDate.now()))
                .and(dataFineValiditaGreaterThanOrNull(LocalDate.now()))
                .and(dataCancellazioneIsNull())
                .and(ambitoIs(filtro.getAmbito()));
    }

    private void trimApplicazione(ApplicazioneDto applicazione){
        applicazione.setAppDescription(applicazione.getAppDescription().trim());
        applicazione.setAppName(applicazione.getAppName().trim());
        applicazione.setAppUrl(applicazione.getAppUrl().trim());
    }

    @Override
    public List<ApplicazioneDto> searchApplicationsForAggregation() {

        List<Applicazione> applicazioni = applicazioneRepository.getApplicazioniPerAggregazione();
        if(CollectionUtils.isEmpty(applicazioni))
            return new ArrayList<>();

        List<ApplicazioneDto> dtos = applicazioni.stream()
                .map(el -> {
                    ApplicazioneDto temp = applicazioneMapper.toDto(el);
                    temp.setRuoliApplicativi(groupsService.findAllByAppId(el.getAppId()));
                    temp.getRuoliApplicativi().forEach(ruolo -> ruolo.setRegoleSicurezza(!regolaSicurezzaRepository.getRegoleByNomeRuoloAndAppId(ruolo.getNome(), ruolo.getApp()).isEmpty()));
                    return temp;
                })
                .toList();

        return dtos;
    }

}

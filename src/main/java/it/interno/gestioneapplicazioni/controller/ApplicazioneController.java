package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.ApplicazioneDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.dto.filter.ApplicazioneFilterDto;
import it.interno.gestioneapplicazioni.service.ApplicazioneService;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import it.interno.gestioneapplicazioni.validation.ValidazioneAggiornamento;
import it.interno.gestioneapplicazioni.validation.ValidazioneInserimento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/applicazione", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class ApplicazioneController {

    @Autowired
    private ApplicazioneService applicazioneService;


    @Operation(description = "API per recuperare un'applicazione dato l'id")
    @GetMapping("/{idApplicazione}")
    public ResponseEntity<ResponseDto<ApplicazioneDto>> getById(@PathVariable @NotBlank String idApplicazione){
        ApplicazioneDto applicazione = applicazioneService.getById(idApplicazione);

        return ResponseEntity.ok(ResponseDto.<ApplicazioneDto>builder()
                .code(HttpStatus.OK.value())
                .body(applicazione)
                .build());
    }

    @Operation(description = "API per recuperare tutte le applicazioni")
    @GetMapping()
    public ResponseEntity<ResponseDto<List<ApplicazioneDto>>> getAllApplicazioni(){

        List<ApplicazioneDto> result = applicazioneService.getAllApplicazioni();

        return ResponseEntity.ok(ResponseDto.<List<ApplicazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(description = "API per recuperare tutte le applicazioni di un determinato ambito")
    @GetMapping("/ambito")
    public ResponseEntity<ResponseDto<List<ApplicazioneDto>>> getAllApplicazioniByAmbito(@RequestParam(required = false) Integer idAmbito){

        List<ApplicazioneDto> result = applicazioneService.getAllApplicazioniByAmbito(idAmbito);

        return ResponseEntity.ok(ResponseDto.<List<ApplicazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(description = "API per ordinare le applicazioni")
    @PostMapping("/ordinamento")
    @Validated(ValidazioneAggiornamento.class)
    public ResponseEntity<ResponseDto<List<ApplicazioneDto>>> ordinamentoApplicazioni(@RequestBody @NotNull List<ApplicazioneDto> input,
                                                                                      @RequestParam @NotBlank String utente,
                                                                                      @RequestParam @NotBlank String ufficio){

        List<ApplicazioneDto> result = applicazioneService.ordinamentoApplicazioni(input, utente, ufficio, ConversionUtils.getCurrentTimestamp());

        return ResponseEntity.ok(ResponseDto.<List<ApplicazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(description = "API per eseguire la Search and Paginate di tutte le Applicazioni")
    @PostMapping("/search-and-paginate")
    public ResponseEntity<ResponseDto<Page<ApplicazioneDto>>> getAllApplicazioniPaginate(
            @RequestBody @Valid ApplicazioneFilterDto filtro){

        Page<ApplicazioneDto> result = applicazioneService.searchAndPaginate(filtro);

        return ResponseEntity.ok(ResponseDto.<Page<ApplicazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(description = "API per eseguire l'inserimento di un'applicazione")
    @PostMapping()
    @Validated(ValidazioneInserimento.class)
    public ResponseEntity<ResponseDto<ApplicazioneDto>> insertNew(@RequestBody @Valid ApplicazioneDto input,
                                                                  @RequestParam String utenteInserimento,
                                                                  @RequestParam String ufficioInserimento){

        ApplicazioneDto result = applicazioneService.insertNew(input, utenteInserimento, ufficioInserimento, ConversionUtils.getCurrentTimestamp());

        return ResponseEntity.ok(ResponseDto.<ApplicazioneDto>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(description = "API per eseguire la modifica di un'applicazione")
    @PutMapping()
    @Validated(ValidazioneAggiornamento.class)
    public ResponseEntity<ResponseDto<ApplicazioneDto>> updateApplicazione(@RequestBody @Valid ApplicazioneDto input,
                                                                           @RequestParam @NotBlank String utenteAggiornamento,
                                                                           @RequestParam @NotBlank String ufficioAggiornamento){

        ApplicazioneDto result = applicazioneService.updateApplicazione(input, utenteAggiornamento, ufficioAggiornamento, ConversionUtils.getCurrentTimestamp());

        return ResponseEntity.ok(ResponseDto.<ApplicazioneDto>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(description = "API per la cancellazione logica di un applicazione")
    @DeleteMapping("/{idApplicazione}")
    public ResponseEntity<ResponseDto<ApplicazioneDto>> deleteApplicazione(
            @PathVariable @NotBlank String idApplicazione,
            @RequestParam @NotBlank String utenteCancellazione,
            @RequestParam String ufficioCancellazione){

        ApplicazioneDto result = applicazioneService.deleteApplicazione(idApplicazione, utenteCancellazione, ufficioCancellazione, ConversionUtils.getCurrentTimestamp());

        return ResponseEntity.ok(ResponseDto.<ApplicazioneDto>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(description = "API per recuperare tutte le applicazioni che hanno almeno un ruolo e una regola di sicurezza")
    @GetMapping("/search-applications-for-aggregation")
    public ResponseEntity<ResponseDto<List<ApplicazioneDto>>> getAllApplicazionsForAggregation(){

        List<ApplicazioneDto> result = applicazioneService.searchApplicationsForAggregation();

        return ResponseEntity.ok(ResponseDto.<List<ApplicazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

}

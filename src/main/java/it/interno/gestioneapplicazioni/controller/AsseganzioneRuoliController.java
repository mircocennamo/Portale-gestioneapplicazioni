package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.AssegnazioneRuoliDto;
import it.interno.gestioneapplicazioni.dto.PaginazioneDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.service.AssegnazioneRuoliService;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/assegnazione-ruoli", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AsseganzioneRuoliController {

    @Autowired
    private AssegnazioneRuoliService assegnazioneRuoliService;

    private static final String OAM_REMOTE_USER_HEADER = "OAM_REMOTE_USER";

    @Operation(summary = "API per recuperare la lista delle applicazioni con ruoli che possono essere assegnati ad un utente")
    @PostMapping("/lista-applicazioni-assegnabili")
    public ResponseEntity<ResponseDto<Page<AssegnazioneRuoliDto>>> getListaApplicazioniAssegnabili(@RequestParam String codiceUtente,
                                                                                                   @RequestParam Integer flagRicerca,
                                                                                                   @RequestParam String parametroRicerca,
                                                                                                   @RequestParam(required = false) Integer ambito,
                                                                                                   @RequestBody PaginazioneDto paginazione,
                                                                                                   @RequestHeader(OAM_REMOTE_USER_HEADER) String oamRemoteUser){

        return ResponseEntity.ok(ResponseDto.<Page<AssegnazioneRuoliDto>>builder()
                .code(HttpStatus.OK.value())
                .body(assegnazioneRuoliService.getListaApplicazioniAssegnabili(codiceUtente, flagRicerca, parametroRicerca, ambito, paginazione,oamRemoteUser))
                .build());
    }

    @Operation(summary = "API per salvare i ruoli associati ad un utente")
    @PostMapping("/assegnazione-ruoli")
    public ResponseEntity<ResponseDto<AssegnazioneRuoliDto>> salvataggioRuoliAdUtente(@RequestParam String utente,
                                                                                      @RequestParam String ufficio,
                                                                                      @RequestBody AssegnazioneRuoliDto input){

        return ResponseEntity.ok(ResponseDto.<AssegnazioneRuoliDto>builder()
                .code(HttpStatus.OK.value())
                .body(assegnazioneRuoliService.salvataggioRuoliAdUtente(input, utente, ufficio, ConversionUtils.getCurrentTimestamp()))
                .build());
    }

}

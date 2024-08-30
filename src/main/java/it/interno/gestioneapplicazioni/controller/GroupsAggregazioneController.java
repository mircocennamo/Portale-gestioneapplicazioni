package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.GroupsAggregazioneDto;
import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.dto.PaginazioneDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.service.GroupsAggregazioneService;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/groups-aggregazione", produces = {MediaType.APPLICATION_JSON_VALUE})
public class GroupsAggregazioneController {

    @Autowired
    private GroupsAggregazioneService groupsAggregazioneService;

    @Operation(summary = "API per recuperare la lista di ruoli aggregati")
    @PostMapping("/lista")
    public ResponseEntity<ResponseDto<Page<GroupsAggregazioneDto>>> getListaRuoliAggregati(@RequestBody PaginazioneDto paginazione){

        return ResponseEntity.ok(ResponseDto.<Page<GroupsAggregazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(groupsAggregazioneService.getListaRuoliAggregati(paginazione))
                .build()
        );
    }

    @Operation(summary = "API per recuperare il dettaglio dell'aggregazione dato il ruolo principale")
    @GetMapping("/by-principale")
    public ResponseEntity<ResponseDto<List<GroupsAggregazioneDto>>> getRuoliAggregatiByPrincipale(@RequestParam String applicazionePrincipale,
                                                                                                  @RequestParam String ruoloPrincipale){

        return ResponseEntity.ok(ResponseDto.<List<GroupsAggregazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(groupsAggregazioneService.getRuoliAggregatiByPrincipale(applicazionePrincipale, ruoloPrincipale))
                .build()
        );
    }

    @Operation(summary = "API per recuperare il dettaglio dell'aggregazione dato il ruolo principale e l'aggregato")
    @GetMapping("/dettaglio")
    public ResponseEntity<ResponseDto<GroupsAggregazioneDto>> getDettaglioAggregazione(@RequestParam String applicazionePrincipale,
                                                                                       @RequestParam String ruoloPrincipale,
                                                                                       @RequestParam String applicazioneDipendente,
                                                                                       @RequestParam String ruoloDipendente){

        return ResponseEntity.ok(ResponseDto.<GroupsAggregazioneDto>builder()
                .code(HttpStatus.OK.value())
                .body(groupsAggregazioneService.getDettaglioAggregazione(applicazionePrincipale, ruoloPrincipale, applicazioneDipendente, ruoloDipendente))
                .build()
        );
    }

    @Operation(summary = "API per inserire un ruolo aggregato")
    @PostMapping
    public ResponseEntity<ResponseDto<GroupsAggregazioneDto>> inserimento(@RequestBody GroupsAggregazioneDto input,
                                                                                      @RequestParam String utente,
                                                                                      @RequestParam String ufficio){

        return ResponseEntity.ok(ResponseDto.<GroupsAggregazioneDto>builder()
                .code(HttpStatus.OK.value())
                .body(groupsAggregazioneService.inserimento(input, utente, ufficio, ConversionUtils.getCurrentTimestamp()))
                .build()
        );
    }

    @Operation(summary = "API per aggiornare un ruolo aggregato")
    @PutMapping
    public ResponseEntity<ResponseDto<GroupsAggregazioneDto>> aggiornamento(@RequestBody GroupsAggregazioneDto input,
                                                                          @RequestParam String utente,
                                                                          @RequestParam String ufficio,
                                                                            @RequestParam String idAppDipendente,
                                                                            @RequestParam String ruoloDipendente){

        return ResponseEntity.ok(ResponseDto.<GroupsAggregazioneDto>builder()
                .code(HttpStatus.OK.value())
                .body(groupsAggregazioneService.aggiornamento(input, utente, ufficio, idAppDipendente,ruoloDipendente,ConversionUtils.getCurrentTimestamp()))
                .build()
        );
    }

    @Operation(summary = "API per aggiornare un ruolo aggregato")
    @DeleteMapping
    public ResponseEntity<ResponseDto<GroupsAggregazioneDto>> cancellazione(@RequestParam String appPrincipale,
                                                                            @RequestParam String ruoloPrincipale,
                                                                            @RequestParam String appDipendente,
                                                                            @RequestParam String ruoloDipendente,
                                                                            @RequestParam String utente,
                                                                            @RequestParam String ufficio){

        groupsAggregazioneService.cancellazione(appPrincipale, ruoloPrincipale, appDipendente, ruoloDipendente, utente, ufficio, ConversionUtils.getCurrentTimestamp());
        return ResponseEntity.ok(ResponseDto.<GroupsAggregazioneDto>builder()
                .code(HttpStatus.OK.value())
                .build()
        );
    }

    @Operation(summary = "API per recuperare la lista dei ruoli principali selezionabili in aggregazione")
    @GetMapping("/groups/principali")
    public ResponseEntity<ResponseDto<List<GroupsDto>>> getRuoliPrincipaliAggregazione(@RequestParam String appId){

        return ResponseEntity.ok(ResponseDto.<List<GroupsDto>>builder()
                .code(HttpStatus.OK.value())
                .body(groupsAggregazioneService.getRuoliPrincipaliAggregazione(appId))
                .build());
    }

    @Operation(summary = "API per recuperare la lista dei ruoli dipendenti selezionabili in aggregazione")
    @GetMapping("/groups/dipendenti")
    public ResponseEntity<ResponseDto<List<GroupsDto>>> getRuoliDipendentiAggregazione(@RequestParam String appId,
                                                                                       @RequestParam String appIdPrincipale,
                                                                                       @RequestParam String ruoloPrincipale){

        return ResponseEntity.ok(ResponseDto.<List<GroupsDto>>builder()
                .code(HttpStatus.OK.value())
                .body(groupsAggregazioneService.getRuoliDipendentiAggregazione(appId, appIdPrincipale, ruoloPrincipale))
                .build());
    }
}

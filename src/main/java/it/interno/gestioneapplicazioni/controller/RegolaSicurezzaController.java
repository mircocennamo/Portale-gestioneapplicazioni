package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.DuplicazioneRegoleDto;
import it.interno.gestioneapplicazioni.dto.RegolaSicurezzaDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.service.RegolaSicurezzaService;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/regola-sicurezza", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RegolaSicurezzaController {

    @Autowired
    private RegolaSicurezzaService regolaSicurezzaService;

    @Operation(summary = "API per recuperare la lista delle regole di sicurezza di un ruolo")
    @GetMapping("/lista")
    public ResponseEntity<ResponseDto<List<RegolaSicurezzaDto>>> getAllRegoleByRuolo(@RequestParam String nomeRuolo,
                                                                                     @RequestParam String appId){

        return ResponseEntity.ok(ResponseDto.<List<RegolaSicurezzaDto>>builder()
                .code(HttpStatus.OK.value())
                .body(regolaSicurezzaService.getAllRegoleByRuolo(nomeRuolo, appId))
                .build());
    }

    @Operation(summary = "API per recuperare il dettaglio della regola di un ruolo")
    @GetMapping
    public ResponseEntity<ResponseDto<RegolaSicurezzaDto>> getRegolaByRuoloAndIdRegola(@RequestParam String nomeRuolo,
                                                                                       @RequestParam String appId,
                                                                                       @RequestParam Integer idRegola){

        return ResponseEntity.ok(ResponseDto.<RegolaSicurezzaDto>builder()
                .code(HttpStatus.OK.value())
                .body(regolaSicurezzaService.getRegolaByRuoloAndIdRegola(nomeRuolo, appId, idRegola))
                .build());
    }

    @Operation(summary = "API per il salvataggio delle modifiche per le regole")
    @PostMapping("/salvataggio")
    public ResponseEntity<ResponseDto<List<RegolaSicurezzaDto>>> salvataggio(@RequestBody List<RegolaSicurezzaDto> input,
                                                                       @RequestParam String utente,
                                                                       @RequestParam String ufficio){

        return ResponseEntity.ok(ResponseDto.<List<RegolaSicurezzaDto>>builder()
                .code(HttpStatus.OK.value())
                .body(regolaSicurezzaService.salvataggio(input, utente, ufficio, ConversionUtils.getCurrentTimestamp()))
                .build());
    }

    @Operation(summary = "API per cancellare tutte le regole di sicurezza di un ruolo")
    @DeleteMapping("/all-by-ruolo")
    public ResponseEntity<ResponseDto<Object>> deleteAll(@RequestParam String nomeRuolo,
                                                         @RequestParam String idApplicazione,
                                                         @RequestParam String utente,
                                                         @RequestParam String ufficio){

        regolaSicurezzaService.deleteAll(nomeRuolo, idApplicazione, utente, ufficio, ConversionUtils.getCurrentTimestamp());
        return ResponseEntity.ok(ResponseDto.builder()
                .code(HttpStatus.OK.value())
                .build());
    }

    @Operation(summary = "API per duplicare le regole di sicurezza in altri ruoli")
    @PostMapping("/duplicazione")
    public ResponseEntity<ResponseDto<Object>> duplicazioneRegole(@RequestParam String utente,
                                                                  @RequestParam String ufficio,
                                                                  @RequestBody DuplicazioneRegoleDto input){

        regolaSicurezzaService.duplicazioneRegole(input, utente, ufficio, ConversionUtils.getCurrentTimestamp());
        return ResponseEntity.ok(ResponseDto.builder()
                .code(HttpStatus.OK.value())
                .build());
    }

}

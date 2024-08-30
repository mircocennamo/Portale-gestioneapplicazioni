package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.dto.TipoMotivazioneDto;
import it.interno.gestioneapplicazioni.service.TipoMotivazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tipo-motivazione", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TipoMotivazioneController {

    @Autowired
    private TipoMotivazioneService tipoMotivazioneService;

    @Operation(description = "API per recuperare la lista di tipologia di motivazione")
    @GetMapping()
    public ResponseEntity<ResponseDto<List<TipoMotivazioneDto>>> getAllTipiMotivazione(){

        List<TipoMotivazioneDto> result = tipoMotivazioneService.getAllTipiMotivazione();

        return ResponseEntity.ok(ResponseDto.<List<TipoMotivazioneDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());

    }

}

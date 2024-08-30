package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.QualificaAssegnabilitaDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.service.QualificaAssegnabilitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/qualifica-assegnabilita", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QualificaAssegnabilitaController {

    @Autowired
    private QualificaAssegnabilitaService qualificaAssegnabilitaService;

    @Operation(description = "API per recuperare la lista di qualifiche")
    @GetMapping()
    public ResponseEntity<ResponseDto<List<QualificaAssegnabilitaDto>>> getAll(){

        List<QualificaAssegnabilitaDto> result = qualificaAssegnabilitaService.getAll();

        return ResponseEntity.ok(ResponseDto.<List<QualificaAssegnabilitaDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

}

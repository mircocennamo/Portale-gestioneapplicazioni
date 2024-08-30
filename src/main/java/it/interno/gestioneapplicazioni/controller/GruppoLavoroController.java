package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.GruppoLavoroDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.service.GruppoLavoroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/gruppo-lavoro", produces = {MediaType.APPLICATION_JSON_VALUE})
public class GruppoLavoroController {

    @Autowired
    private GruppoLavoroService gruppoLavoroService;

    @Operation(summary = "API per recuperare la lista dei gruppi di lavoro")
    @GetMapping
    public ResponseEntity<ResponseDto<List<GruppoLavoroDto>>> getAllGruppoLavoro(){

        return ResponseEntity.ok(ResponseDto.<List<GruppoLavoroDto>>builder()
                .code(HttpStatus.OK.value())
                .body(gruppoLavoroService.getAllGruppoLavoro())
                .build());
    }


}

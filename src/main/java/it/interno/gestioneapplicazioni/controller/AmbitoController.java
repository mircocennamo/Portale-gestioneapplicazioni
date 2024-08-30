package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.AmbitoDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.service.AmbitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/ambito", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AmbitoController {

    @Autowired
    private AmbitoService ambitoService;

    @Operation(description = "API per recuperare tutti gli ambiti")
    @GetMapping()
    public ResponseEntity<ResponseDto<List<AmbitoDto>>> getAllAmbiti(){

        List<AmbitoDto> result = ambitoService.getAll();

        return ResponseEntity.ok(ResponseDto.<List<AmbitoDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

}

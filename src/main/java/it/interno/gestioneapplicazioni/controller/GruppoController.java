package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.GruppoDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.service.GruppoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping(value = "/gruppo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class GruppoController {

    @Autowired
    private GruppoService gruppoService;

    @Operation(description = "API per recuperare la lista di gruppi per tipo")
    @GetMapping("/{type}")
    public ResponseEntity<ResponseDto<List<GruppoDto>>> getAllByType(@PathVariable @NotBlank String type){

        List<GruppoDto> result = gruppoService.getAllByType(type);

        return ResponseEntity.ok(ResponseDto.<List<GruppoDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

}

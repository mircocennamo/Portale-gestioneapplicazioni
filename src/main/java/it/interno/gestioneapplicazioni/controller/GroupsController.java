package it.interno.gestioneapplicazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.dto.RuoliPerRegoleSicurezzaDto;
import it.interno.gestioneapplicazioni.dto.filter.ApplicazioneFilterDto;
import it.interno.gestioneapplicazioni.service.ApplicazioneMotivMembersService;
import it.interno.gestioneapplicazioni.service.AssegnazioneRuoliService;
import it.interno.gestioneapplicazioni.service.GroupsService;
import it.interno.gestioneapplicazioni.service.RegolaSicurezzaService;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/groups", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private AssegnazioneRuoliService assegnazioneRuoliService;

    @Autowired
    private RegolaSicurezzaService regolaSicurezzaService;

    @Autowired
    private ApplicazioneMotivMembersService applicazioneMotivMembersService;

    @Operation(description = "API per recuperare la lista di groups dato l'id dell'app")
    @GetMapping("/{appId}")
    public ResponseEntity<ResponseDto<List<GroupsDto>>> findAllByAppId(@PathVariable @NotBlank String appId){

        List<GroupsDto> result = groupsService.findAllByAppId(appId);

        return ResponseEntity.ok(ResponseDto.<List<GroupsDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());

    }

    @Operation(description = "API per persistere i ruoli applicativi")
    @PostMapping()
    public ResponseEntity<ResponseDto<List<GroupsDto>>> salvataggioGroup(@RequestBody List<@Valid GroupsDto> input,
                                                                         @RequestParam String utente,
                                                                         @RequestParam String ufficio){

        List<GroupsDto> result = groupsService.salvataggio(input, utente, ufficio, ConversionUtils.getCurrentTimestamp());

        return ResponseEntity.ok(ResponseDto.<List<GroupsDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());

    }

    @Operation(description = "API per eliminare tutti i ruoli applicativi di un applicazione")
    @DeleteMapping("/{appId}")
    public ResponseEntity<ResponseDto<List<GroupsDto>>> deleteAllGroups(@PathVariable String appId,
                                                                        @RequestParam String utente,
                                                                        @RequestParam String ufficio){

        Timestamp data = ConversionUtils.getCurrentTimestamp();
        //GROUP_MEMBERS
        assegnazioneRuoliService.deleteAll(appId,utente,ufficio,data);

        //SEC_REGOLE_SICUREZZA
        regolaSicurezzaService.deleteAll(appId,utente,ufficio,data);

        groupsService.deleteAllByApp(appId, utente, ufficio, data);

        applicazioneMotivMembersService.deleteByAppId(appId, utente, ufficio, data);

        return ResponseEntity.ok(ResponseDto.<List<GroupsDto>>builder()
                .code(HttpStatus.OK.value())
                .build());

    }

    @Operation(description = "API per eseguire la Search and Paginate di per la lista di ruoli per regole di sicurezza")
    @PostMapping("/search-and-paginate-regole-sicurezza")
    public ResponseEntity<ResponseDto<Page<RuoliPerRegoleSicurezzaDto>>> getAllApplicazioniPaginateSegoleSicurezza(
            @RequestBody @Valid ApplicazioneFilterDto filtro){

        Page<RuoliPerRegoleSicurezzaDto> result = groupsService.searchAndPaginateRegoleSicurezza(filtro.getFlagRicerca(), filtro.getParametroRicerca(), filtro);

        return ResponseEntity.ok(ResponseDto.<Page<RuoliPerRegoleSicurezzaDto>>builder()
                .code(HttpStatus.OK.value())
                .body(result)
                .build());
    }

    @Operation(summary = "API per recuperare l'estratto dei ruoli in autocomplete per le regole di sicurezza")
    @GetMapping("/autocomplete")
    public ResponseEntity<ResponseDto<List<RuoliPerRegoleSicurezzaDto>>> findAllByAutocompleteRegole(@RequestParam String parametroRicerca,
                                                                                                     @RequestParam List<String> ruoliDaEscludere){

        return ResponseEntity.ok(ResponseDto.<List<RuoliPerRegoleSicurezzaDto>>builder()
                .code(HttpStatus.OK.value())
                .body(groupsService.findAllByAutocomplete(parametroRicerca, ruoliDaEscludere))
                .build());
    }
}

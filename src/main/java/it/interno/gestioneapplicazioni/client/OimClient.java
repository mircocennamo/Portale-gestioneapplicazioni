package it.interno.gestioneapplicazioni.client;

import it.interno.gestioneapplicazioni.dto.oim.RuoloOimDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "oim", path = "/oim")
public interface OimClient {

    @PutMapping("/ruolo/rename")
    void renameRuolo(@RequestParam String nomeRuoloOld, @RequestParam String nomeRuoloNew);

    @PostMapping("/ruolo-utente/associazione")
    void associazioneRuolo(@RequestParam String codiceUtente, @RequestParam String nomeRuolo);

    @PostMapping("/ruolo-utente/disassociazione")
    void rimozioneRuolo(@RequestParam String codiceUtente, @RequestParam String nomeRuolo);

    @PostMapping("/ruolo/massivo")
    void creazioneRuoli(@RequestBody List<RuoloOimDto> input);

    @PutMapping("/ruolo/massivo")
    void modificaRuoli(@RequestBody List<RuoloOimDto> input);

    @PostMapping("/ruolo/delete/massivo")
    void deleteRuoli(@RequestBody List<String> nomiRuoli);

    @GetMapping("/ruolo")
    public String ricercaRuoloId(@RequestParam String nomeRuolo);

    @PostMapping("/ruolo-utente/disassociazione/utenti")
    void rimozioneRuoloAUtenti(@RequestParam String nomeRuolo, @RequestBody List<String> codiciUtenti);
}

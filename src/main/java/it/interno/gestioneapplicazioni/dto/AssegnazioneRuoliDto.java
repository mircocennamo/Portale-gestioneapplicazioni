package it.interno.gestioneapplicazioni.dto;

import it.interno.gestioneapplicazioni.entity.Groups;
import it.interno.gestioneapplicazioni.entity.TipoMotivazione;
import it.interno.gestioneapplicazioni.repository.ApplicMotivMembersRepository;
import it.interno.gestioneapplicazioni.repository.GroupMembersRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssegnazioneRuoliDto {
    private String codiceUtente;
    private String idApplicazione;
    private String nomeApplicazione;
    private String descrizioneApplicazione;
    private String macroCategoria;
    private List<RuoliAssegnabiliDto> ruoliAssegnabili = new ArrayList<>();
    private List<MotivazioniAssegnabiliDto> motivazioniAssegnabili = new ArrayList<>();

    public AssegnazioneRuoliDto(String codiceUtente, String idApplicazione, String nomeApplicazione, String descrizioneApplicazione, String macroCategoria) {
        this.codiceUtente = codiceUtente;
        this.idApplicazione = idApplicazione;
        this.nomeApplicazione = nomeApplicazione;
        this.descrizioneApplicazione = descrizioneApplicazione;
        this.macroCategoria = macroCategoria;
    }

    public void setRuoliAssegnabili(List<Groups> input, String codiceUtente, GroupMembersRepository groupMembersRepository){
        input.forEach(el ->
            this.ruoliAssegnabili.add(new RuoliAssegnabiliDto(
                    el.getNome(),
                    el.getDescrizione(),
                    el.getTipo(),
                    groupMembersRepository.getByUtenteAndRuolo(codiceUtente, el.getNome(), idApplicazione) != null
            ))
        );
    }

    public void setMotivazioniAssegnabili(List<TipoMotivazione> input, String codiceUtente, ApplicMotivMembersRepository applicMotivMembersRepository){
        input.forEach(el ->
            this.motivazioniAssegnabili.add(new MotivazioniAssegnabiliDto(
                    el.getIdTipoMotivazione(),
                    el.getDescrizione().trim(),
                    applicMotivMembersRepository.getByUtenteAppETipoMotivazione(codiceUtente, this.idApplicazione, el.getIdTipoMotivazione()) != null
            ))
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class RuoliAssegnabiliDto {
        private String nomeRuolo;
        private String descrizioneRuolo;
        private String tipoRuolo;
        private boolean assegnato;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class MotivazioniAssegnabiliDto {
        private Integer idTipoMotivazione;
        private String descrizioneMotivazione;
        private boolean assegnato;
    }
}

package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMotivazioneDto {
    private Integer idTipoMotivazione;
    private Timestamp tsInserimento;
    private String descrizione;
    private String idUtenteInserimento;
    private String idUfficioInserimento;
    private Timestamp tsCancellazione;
    private String idUtenteCancellazione;
    private String idUfficioCancellazione;
    private Character flagDettaglioSingolo;
}

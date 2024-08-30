package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicazioneMotivazioneDto {
    private String idApp;
    private Integer idTipoMotivazione;
    private Timestamp dataInserimento;
    private String utenteInserimento;
    private String ufficioInserimento;
    private String utentecancellazione;
    private Timestamp dataCancellazione;
}

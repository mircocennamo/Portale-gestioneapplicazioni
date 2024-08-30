package it.interno.gestioneapplicazioni.entity;

import it.interno.gestioneapplicazioni.entity.key.TipoMotivazioneKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SEC_TIPO_MOTIVAZIONE", schema = "SSD_SECURITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TipoMotivazioneKey.class)
public class TipoMotivazione {

    @Id
    @Column(name = "ID_TIPO_MOTIVAZIONE")
    private Integer idTipoMotivazione;
    @Id
    @Column(name = "TSINSERIMENTO")
    private Timestamp tsInserimento;

    @Column(name = "DES_MOTIVAZIONE")
    private String descrizione;
    @Column(name = "IDUTENTEINSERIMENTO")
    private String idUtenteInserimento;
    @Column(name = "IDUFFICIOINSERIMENTO")
    private String idUfficioInserimento;
    @Column(name = "TSCANCELLAZIONE")
    private Timestamp tsCancellazione;
    @Column(name = "IDUTENTECANCELLAZIONE")
    private String idUtenteCancellazione;
    @Column(name = "IDUFFICIOCANCELLAZIONE")
    private String idUfficioCancellazione;
    @Column(name = "FLG_DETT_SINGOLO")
    private Character flagDettaglioSingolo;
    @Column(name = "VISIBILITA_MOTIVAZIONE")
    private Character visibilitaMotivazione;

}

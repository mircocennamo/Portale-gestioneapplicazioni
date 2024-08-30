package it.interno.gestioneapplicazioni.entity.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMotivazioneKey implements Serializable {
    private Integer idTipoMotivazione;
    private Timestamp tsInserimento;
}

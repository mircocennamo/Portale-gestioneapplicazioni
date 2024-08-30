package it.interno.gestioneapplicazioni.entity.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbitoKey implements Serializable {

    private String descrizioneAmbito;
    private Integer idOrdinamento;

}

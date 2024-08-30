package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbitoDto {
    private String descrizioneAmbito;
    private String icona;
    private Integer idOrdinamento;
}

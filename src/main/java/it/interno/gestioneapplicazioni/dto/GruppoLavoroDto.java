package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GruppoLavoroDto {
    private Integer idGruppoLavoro;
    private String descrizione;
}

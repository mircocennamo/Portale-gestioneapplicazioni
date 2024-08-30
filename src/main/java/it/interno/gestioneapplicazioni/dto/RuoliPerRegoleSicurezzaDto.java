package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuoliPerRegoleSicurezzaDto {

    private String nomeApplicazione;
    private String appId;
    private String nomeRuolo;
    private boolean regoleSicurezza;

}

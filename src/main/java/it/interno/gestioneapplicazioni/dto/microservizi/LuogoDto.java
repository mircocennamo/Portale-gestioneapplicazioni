package it.interno.gestioneapplicazioni.dto.microservizi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LuogoDto {
    private Integer codiceLuogo;
    private String descrizioneLuogo;
    private String inLuogo;
    private String codiceRegione;
    private String codiceProvincia;
    private String siglaProvincia;
}

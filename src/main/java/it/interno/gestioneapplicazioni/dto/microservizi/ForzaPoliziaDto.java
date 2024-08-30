package it.interno.gestioneapplicazioni.dto.microservizi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForzaPoliziaDto {
    private Integer idGruppo;
    private String nome;
}

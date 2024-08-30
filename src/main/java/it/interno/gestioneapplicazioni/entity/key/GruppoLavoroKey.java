package it.interno.gestioneapplicazioni.entity.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GruppoLavoroKey implements Serializable {
    private Integer idGruppoLavoro;
    private String codiceUfficio;
}

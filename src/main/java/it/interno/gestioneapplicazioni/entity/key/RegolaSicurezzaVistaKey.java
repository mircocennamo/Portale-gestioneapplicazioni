package it.interno.gestioneapplicazioni.entity.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegolaSicurezzaVistaKey implements Serializable {
    private String nomeRuolo;
    private String appId;
    private Integer idRegola;
    private Integer idProgressivoRegola;
}

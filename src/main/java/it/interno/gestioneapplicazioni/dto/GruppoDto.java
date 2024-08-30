package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GruppoDto {
    private String nome;
    private Integer id;
    private String descrizione;
    private String tipo;
    private String longDescription;
}

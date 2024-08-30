package it.interno.gestioneapplicazioni.dto.microservizi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UfficioDto {
    private String codiceUfficio;
    private String descrizioneUfficio;
    private CategoriaUfficioDto categoriaUfficio;
    private LuogoDto luogoUfficio;
}

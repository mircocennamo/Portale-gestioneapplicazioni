package it.interno.gestioneapplicazioni.dto.microservizi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoriaUfficioDto {
    private String codiceCategoriaUfficio;
    private ForzaPoliziaDto forzaPolizia;
}

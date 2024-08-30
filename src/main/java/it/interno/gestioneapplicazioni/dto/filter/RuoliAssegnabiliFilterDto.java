package it.interno.gestioneapplicazioni.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuoliAssegnabiliFilterDto {
    private String parametroRicerca;
    private String macroCategoria;
    private String sortBy;
    private String sortDirection;
}

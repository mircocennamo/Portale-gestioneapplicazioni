package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginazioneDto {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortDirection;
}

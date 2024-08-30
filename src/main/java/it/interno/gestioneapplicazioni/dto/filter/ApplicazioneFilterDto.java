package it.interno.gestioneapplicazioni.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicazioneFilterDto {

    @NotNull(message = "Il paramentro di ricerca deve essere valorizzato")
    private String parametroRicerca;

    private int flagRicerca;

    @NotNull(message = "Il numero di pagina deve essere valorizzato")
    @Min(value = 0, message = "Il numero di pagina non può essere minore di 0")
    private Integer pagina;

    @NotNull(message = "Il numero di elementi deve essere valorizzato")
    @Min(value = 1, message = "Il numero di elementi non può essere minore di 1")
    private Integer numeroElementi;

    private Integer ambito;

    @NotBlank(message = "Il parametro di ordinamento deve essere valorizzato")
    private String sortBy;

    @NotBlank(message = "L'ordine di ordinamento deve essere valorizzato")
    private String sortDirection;

}

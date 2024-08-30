package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IdEDescrizioneDto<T>{
    private T id;
    private String descrizione;
    private String descrizioneShort;

    public IdEDescrizioneDto(T id, String descrizone) {
        this.id = id;
        this.descrizione = descrizone;
    }
}

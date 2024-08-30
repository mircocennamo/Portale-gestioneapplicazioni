package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualificaAssegnabilitaDto {
    private Integer id;
    private String descrizioneQualifica;

    public QualificaAssegnabilitaDto(Integer id) {
        this.id = id;
    }
}

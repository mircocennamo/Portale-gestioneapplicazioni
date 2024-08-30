package it.interno.gestioneapplicazioni.dto;

import it.interno.gestioneapplicazioni.entity.key.GroupsKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DuplicazioneRegoleDto {

    private GroupsKey ruoloSorgente;
    private List<GroupsKey> ruoliDestinazione;
}

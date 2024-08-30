package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.entity.Groups;
import it.interno.gestioneapplicazioni.entity.Gruppo;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;

import java.time.LocalDate;
import java.util.List;

public class GroupsMapper {

    private GroupsMapper() {
    }

    public static GroupsDto toDto(Groups input){
        return input == null ? null : new GroupsDto(
            input.getNome(),
            input.getApp(),
            input.getDataInserimento(),
            input.getDescrizione(),
            input.getGruppo(),
            input.getId(),
            input.getTipo(),
            input.getUtenteInserimento(),
            input.getUfficioInserimento(),
            input.getUtenteAggiornamento(),
            input.getUfficioAggiornamento(),
            input.getDataAggiornamento(),
            input.getUtenteCancellazione(),
            input.getUfficioCancellazione(),
            input.getDataCancellazione(),
            input.getDataInizioValidita(),
            input.getDataFineValidita(),
            ConversionUtils.qualificheAssegnateToInteger(input.getQualificheAssegnate()),
            null
        );
    }

    public static Groups toEntity(GroupsDto input, List<Gruppo> gruppi){

        if(input != null && input.getDataFineValidita() == null)
            input.setDataFineValidita(LocalDate.of(9999, 12, 31));

        Gruppo gruppo = gruppi == null ? null : gruppi.stream().filter(el -> el.getNome().equals(input.getGruppo())).findFirst().orElse(new Gruppo());

        return input == null ? null : new Groups(
                input.getNome(),
                input.getApp(),
                input.getDataInserimento(),
                input.getDescrizione(),
                gruppo == null ? null : gruppo.getNome(),
                gruppo == null ? null : gruppo.getId(),
                gruppo == null ? null : gruppo.getTipo(),
                input.getUtenteInserimento(),
                input.getUfficioInserimento(),
                input.getUtenteAggiornamento(),
                input.getUfficioAggiornamento(),
                input.getDataAggiornamento(),
                input.getUtenteCancellazione(),
                input.getUfficioCancellazione(),
                input.getDataCancellazione(),
                input.getDataInizioValidita(),
                input.getDataFineValidita(),
                ConversionUtils.idQualificheAssegnateToEntity(input.getQualificheAssegnate())
        );
    }

}

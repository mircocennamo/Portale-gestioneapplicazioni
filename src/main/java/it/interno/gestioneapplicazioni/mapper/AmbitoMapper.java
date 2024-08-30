package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.AmbitoDto;
import it.interno.gestioneapplicazioni.entity.Ambito;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AmbitoMapper {

    Ambito toEntity(AmbitoDto applicazioneDto);

    @InheritInverseConfiguration
    AmbitoDto toDto(Ambito applicazione);
}

package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.TipoMotivazioneDto;
import it.interno.gestioneapplicazioni.entity.TipoMotivazione;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TipoMotivazioneMapper {

    TipoMotivazione toEntity(TipoMotivazioneDto tipoMotivazioneDto);

    @InheritInverseConfiguration
    TipoMotivazioneDto toDto(TipoMotivazione tipoMotivazione);
}

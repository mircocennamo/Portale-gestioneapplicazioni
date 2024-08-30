package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.ApplicazioneMotivazioneDto;
import it.interno.gestioneapplicazioni.entity.ApplicazioneMotivazione;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ApplicazioneMotivazioneMapper {

    ApplicazioneMotivazione toEntity(ApplicazioneMotivazioneDto applicazioneMotivazioneDto);

    @InheritInverseConfiguration
    ApplicazioneMotivazioneDto toDto(ApplicazioneMotivazione applicazioneMotivazione);
}

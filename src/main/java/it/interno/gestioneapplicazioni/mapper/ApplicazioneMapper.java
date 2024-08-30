package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.ApplicazioneDto;
import it.interno.gestioneapplicazioni.entity.Applicazione;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ApplicazioneMapper {

    Applicazione toEntity(ApplicazioneDto applicazioneDto);

    @InheritInverseConfiguration
    ApplicazioneDto toDto(Applicazione applicazione);
}

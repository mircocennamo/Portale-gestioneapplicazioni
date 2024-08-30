package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.GruppoDto;
import it.interno.gestioneapplicazioni.entity.Gruppo;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface GruppoMapper {
    Gruppo toEntity(GruppoDto gruppoDto);

    @InheritInverseConfiguration
    GruppoDto toDto(Gruppo gruppo);
}

package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.GruppoLavoroDto;
import it.interno.gestioneapplicazioni.entity.GruppoLavoro;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface GruppoLavoroMapper {

    GruppoLavoro toEntity(GruppoLavoroDto gruppoLavoroDto);

    @InheritInverseConfiguration
    GruppoLavoroDto toDto(GruppoLavoro gruppoLavoro);
}

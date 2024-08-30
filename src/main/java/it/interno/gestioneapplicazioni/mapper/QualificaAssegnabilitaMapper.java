package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.QualificaAssegnabilitaDto;
import it.interno.gestioneapplicazioni.entity.QualificaAssegnabilita;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface QualificaAssegnabilitaMapper {

    QualificaAssegnabilita toEntity(QualificaAssegnabilitaDto qualificaAssegnabilitaDto);

    @InheritInverseConfiguration
    QualificaAssegnabilitaDto toDto(QualificaAssegnabilita qualificaAssegnabilita);
}

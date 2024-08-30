package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.GroupsAggregazioneDto;
import it.interno.gestioneapplicazioni.entity.GroupsAggregazione;
import it.interno.gestioneapplicazioni.repository.ApplicazioneRepository;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface GroupsAggregazioneMapper {

    GroupsAggregazione toEntity(GroupsAggregazioneDto dto);

    @InheritInverseConfiguration
    @Mapping(target = "nomeApplicazionePrincipale", expression = "java(appRepo.findAppNameByAppId(entity.getIdAppPrincipale()))")
    GroupsAggregazioneDto toDto(GroupsAggregazione entity, @Context ApplicazioneRepository appRepo);
}

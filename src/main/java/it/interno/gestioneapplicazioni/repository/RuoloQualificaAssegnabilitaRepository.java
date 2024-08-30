package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.RuoloQualificaAssegnabilita;
import it.interno.gestioneapplicazioni.entity.key.RuoloQualificaAssegnabilitaKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuoloQualificaAssegnabilitaRepository extends JpaRepository<RuoloQualificaAssegnabilita, RuoloQualificaAssegnabilitaKey> {

    long deleteByName(String ruolo);




}

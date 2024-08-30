package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.dto.GruppoLavoroDto;
import it.interno.gestioneapplicazioni.entity.GruppoLavoro;
import it.interno.gestioneapplicazioni.entity.key.GruppoLavoroKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GruppoLavoroRepository extends JpaRepository<GruppoLavoro, GruppoLavoroKey> {

    @Query("SELECT DISTINCT new it.interno.gestioneapplicazioni.dto.GruppoLavoroDto(idGruppoLavoro, descrizione) " +
            "FROM GruppoLavoro " +
            "ORDER BY descrizione ASC")
    List<GruppoLavoroDto> getAllGruppoLavoro();

}

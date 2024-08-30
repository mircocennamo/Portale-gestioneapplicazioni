package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.RegolaSicurezzaVista;
import it.interno.gestioneapplicazioni.entity.key.RegolaSicurezzaVistaKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegolaSicurezzaVistaRepository extends JpaRepository<RegolaSicurezzaVista, RegolaSicurezzaVistaKey> {

    @Query("FROM RegolaSicurezzaVista r WHERE r.nomeRuolo = ?1 AND r.appId = ?2")
    List<RegolaSicurezzaVista> getRegoleByNomeRuoloAndAppId(String nomeRuolo, String appId);

    @Query("FROM RegolaSicurezzaVista r WHERE r.nomeRuolo = ?1 AND r.appId = ?2 AND r.idRegola = ?3")
    List<RegolaSicurezzaVista> getRegoleByNomeRuoloAndAppIdAndIdRegola(String nomeRuolo, String appId, Integer idRegola);
}

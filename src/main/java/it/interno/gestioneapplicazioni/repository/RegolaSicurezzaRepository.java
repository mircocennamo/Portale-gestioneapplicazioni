package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.RegolaSicurezza;
import it.interno.gestioneapplicazioni.entity.key.RegolaSicurezzaKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegolaSicurezzaRepository extends JpaRepository<RegolaSicurezza, RegolaSicurezzaKey> {

    @Query("FROM RegolaSicurezza r WHERE r.ruolo = ?1 AND r.appId = ?2 AND r.dataCancellazione IS NULL")
    List<RegolaSicurezza> getRegoleByNomeRuoloAndAppId(String nomeRuolo, String appId);

    @Query("FROM RegolaSicurezza r WHERE r.ruolo = ?1 AND r.appId = ?2 AND r.numeroRegola = ?3 AND r.dataCancellazione IS NULL")
    List<RegolaSicurezza> getRegoleByNomeRuoloAndAppIdAndIdRegola(String nomeRuolo, String appId, Integer idRegola);

    @Query(value = "SELECT NVL(MAX(srs.ID_BLOCCO_REGOLA), 0) + 1 " +
            "FROM SSD_SECURITY.SEC_REGOLE_SICUREZZA srs " +
            "WHERE srs.G_NAME = ?1 AND srs.APP_ID = ?2", nativeQuery = true)
    Integer getNextIdRegolaByNomeRuolo(String nomeRuolo, String appId);

    @Query(value = "SELECT SSD_SECURITY.IS_RUOLO_APPLICATIVO_ASSEGNABILE(:codiceUtente, :idApp, :codiceRuolo) FROM DUAL", nativeQuery = true)
    Integer isRuoloApplicativoAssegnabile(String codiceUtente, String idApp, String codiceRuolo);

    @Query("FROM RegolaSicurezza r WHERE  r.appId = ?1 AND r.dataCancellazione IS NULL")
    List<RegolaSicurezza> getRegoleByAppId(String appId);

}

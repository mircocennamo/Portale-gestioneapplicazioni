package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.TipoMotivazione;
import it.interno.gestioneapplicazioni.entity.key.TipoMotivazioneKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoMotivazioneRepository extends JpaRepository<TipoMotivazione, TipoMotivazioneKey> {

    @Query("FROM TipoMotivazione t " +
            "WHERE t.tsCancellazione IS NULL " +
            "AND t.visibilitaMotivazione = 'S' " +
            "ORDER BY t.idTipoMotivazione ASC")
    List<TipoMotivazione> getAllTipiMotivazione();

    @Query(value = "SELECT m.* " +
            "FROM SSD_SECURITY.SEC_TIPO_MOTIVAZIONE m INNER JOIN SSD_SECURITY.SEC_APPLICAZIONE_MOTIVAZIONE am ON m.ID_TIPO_MOTIVAZIONE = am.ID_TIPO_MOTIVAZIONE " +
            "WHERE am.APP_ID = ?1 " +
            "AND am.DATE_CANC IS NULL", nativeQuery = true)
    List<TipoMotivazione> getAllByApp(String appId);
}

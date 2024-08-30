package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.Applicazione;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicazioneRepository extends PagingAndSortingRepository<Applicazione, String>, JpaSpecificationExecutor<Applicazione>, JpaRepository<Applicazione, String> {

    @Query("FROM Applicazione a WHERE a.appId = ?1 AND a.dataCancellazione IS NULL")
    Optional<Applicazione> findById(String idApplicazione);

    @Query("FROM Applicazione a WHERE a.appName = ?1 AND a.dataCancellazione IS NULL")
    Applicazione findByNome(String nome);

    @Query(value = "SELECT * FROM SSD_SECURITY.SEC_APPLICAZIONE s " +
            "WHERE s.DATE_CANC IS NULL " +
            "ORDER BY s.ORDER_ID_AMBITO, s.ORDER_ID_CATALOGO ASC", nativeQuery = true)
    List<Applicazione> getAllApplicazioni();

    @Query("FROM Applicazione a " +
            "WHERE a.idOrdineAmbito = ?1 " +
            "AND a.dataCancellazione IS NULL " +
            "ORDER BY a.idOrdineCatalogo ASC")
    List<Applicazione> getApplicazioniByAmbito(Integer idAmbito);

    @Query(value = "SELECT MAX(sa.ORDER_ID_CATALOGO) FROM SSD_SECURITY.SEC_APPLICAZIONE sa WHERE sa.ORDER_ID_AMBITO = ?1", nativeQuery = true)
    Optional<Double> getMaxOrderIdCatalogoByAmbito(Integer orderIdCatalogo);

    @Query("FROM Applicazione a WHERE UPPER(TRIM(a.appName)) = UPPER(TRIM(?1)) AND a.dataCancellazione IS NULL")
    List<Applicazione> checkNameAlreadyExists(String appName);

    @Query("FROM Applicazione a WHERE UPPER(TRIM(a.appUrl)) = UPPER(TRIM(?1)) AND a.dataCancellazione IS NULL")
    List<Applicazione> checkURLAlreadyExists(String appUrl);

    @Query("SELECT appName FROM Applicazione WHERE appId = ?1 AND dataCancellazione IS NULL")
    String findAppNameByAppId(String appId);

    @Query(value = "SELECT DISTINCT a.* " +
            "FROM SSD_SECURITY.SEC_APPLICAZIONE a INNER JOIN SSD_SECURITY.GROUPS g ON a.APP_ID = g.G_APP " +
            "   INNER JOIN SSD_SECURITY.SEC_REGOLE_SICUREZZA rs ON a.APP_ID = rs.APP_ID AND g.G_NAME = rs.G_NAME " +
            "INNER JOIN SSD_SECURITY.USERS user ON user.G_MEMBER = ?6" +
            "INNER JOIN SSD_SECURITY.SEC_RUOLO_QUALIF_ASSEGNABILITA ruolo ON user.QUALIFICA = ruolo.QUALIFICA_ASSEGNABILITA_ID " +
            "WHERE " +
            "a.DATE_CANC IS NULL " +
            "AND rs.DATE_CAN IS NULL " +
            "AND g.DATA_CAN IS NULL " +
            "AND (?4 IS NULL OR a.ORDER_ID_AMBITO = ?4) " +
            "AND ((?2 = 0 AND (a.APP_NAME LIKE ?3 OR UPPER(a.APP_DESCRIPTION) LIKE ?3)) OR (?2 = 1 AND g.G_NAME LIKE ?3)) " +
            "AND user.DATA_CAN IS NULL " +
            "AND SSD_SECURITY.IS_RUOLO_APPLICATIVO_ASSEGNABILE(?1, g.G_APP, g.G_NAME) > 0",
            countQuery = "SELECT DISTINCT a.* " +
                    "FROM SSD_SECURITY.SEC_APPLICAZIONE a INNER JOIN SSD_SECURITY.GROUPS g ON a.APP_ID = g.G_APP " +
                    "   INNER JOIN SSD_SECURITY.SEC_REGOLE_SICUREZZA rs ON a.APP_ID = rs.APP_ID AND g.G_NAME = rs.G_NAME " +
                    "INNER JOIN SSD_SECURITY.USERS user ON user.G_MEMBER = ?6" +
                    "INNER JOIN SSD_SECURITY.SEC_RUOLO_QUALIF_ASSEGNABILITA ruolo ON user.QUALIFICA = ruolo.QUALIFICA_ASSEGNABILITA_ID " +
                    "WHERE " +
                    "a.DATE_CANC IS NULL " +
                    "AND rs.DATE_CAN IS NULL " +
                    "AND g.DATA_CAN IS NULL " +
                    "AND user.DATA_CAN IS NULL " +
                    "AND (?4 IS NULL OR a.ORDER_ID_AMBITO = ?4) " +
                    "AND ((?2 = 0 AND (a.APP_NAME LIKE ?3 OR UPPER(a.APP_DESCRIPTION) LIKE ?3)) OR (?2 = 1 AND g.G_NAME LIKE ?3)) " +
                    "AND SSD_SECURITY.IS_RUOLO_APPLICATIVO_ASSEGNABILE(?1, g.G_APP, g.G_NAME) > 0", nativeQuery = true)
    List<Applicazione> getApplicazioniPerAssegnazioneConRuoliAssegnabili(String codiceUtente, Integer flagRicerca, String parametroRicerca, Integer ambito, Pageable pageable,String oamRemoteUser);

    @Query(value = "SELECT COUNT(DISTINCT a.APP_ID) " +
            "FROM SSD_SECURITY.SEC_APPLICAZIONE a INNER JOIN SSD_SECURITY.GROUPS g ON a.APP_ID = g.G_APP " +
            "   INNER JOIN SSD_SECURITY.SEC_REGOLE_SICUREZZA rs ON a.APP_ID = rs.APP_ID AND g.G_NAME = rs.G_NAME " +
            "INNER JOIN SSD_SECURITY.USERS user ON user.G_MEMBER = ?5" +
            "INNER JOIN SSD_SECURITY.SEC_RUOLO_QUALIF_ASSEGNABILITA ruolo ON user.QUALIFICA = ruolo.QUALIFICA_ASSEGNABILITA_ID " +
            "WHERE " +
            "a.DATE_CANC IS NULL " +
            "AND rs.DATE_CAN IS NULL " +
            "AND g.DATA_CAN IS NULL " +
            "AND user.DATA_CAN IS NULL " +
            "AND (?4 IS NULL OR a.ORDER_ID_AMBITO = ?4) " +
            "AND ((?2 = 0 AND (a.APP_NAME LIKE ?3% OR UPPER(a.APP_DESCRIPTION) LIKE ?3%)) OR (?2 = 1 AND g.G_NAME LIKE ?3%)) " +
            "AND SSD_SECURITY.IS_RUOLO_APPLICATIVO_ASSEGNABILE(?1, g.G_APP, g.G_NAME) > 0", nativeQuery = true)
    Integer countTotalApplicazioniAssegnabili(String codiceUtente, Integer flagRicerca, String parametroRicerca, Integer ambito,String oamRemoteUser);

    @Query(value =
            "SELECT * FROM " +
            "SSD_SECURITY.SEC_APPLICAZIONE A " +
            "WHERE " +
            "    A.DATE_CANC IS NULL " +
            "AND EXISTS(SELECT 1 FROM SSD_SECURITY.GROUPS B " +
            "           WHERE " +
            "                B.G_APP = A.APP_ID " +
            "            AND B.DATA_CAN IS NULL " +
            "            AND EXISTS(SELECT 1 FROM SSD_SECURITY.SEC_REGOLE_SICUREZZA C " +
            "                       WHERE " +
            "                            C.APP_ID = B.G_APP " +
            "                        AND C.G_NAME = B.G_NAME " +
            "                        AND C.DATE_CAN IS NULL " +
            "                      ) " +
            "          ) " +
            "ORDER BY A.APP_NAME ASC ", nativeQuery = true)
    List<Applicazione> getApplicazioniPerAggregazione();

}

package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.Groups;
import it.interno.gestioneapplicazioni.entity.key.GroupsKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface GroupsRepository extends JpaRepository<Groups, GroupsKey> {

    @Query("SELECT g FROM Groups g " +
            "WHERE g.app = ?1 " +
            "AND g.dataCancellazione IS NULL")
    List<Groups> findAllByAppId(String appId);

    @Query(value = "SELECT * FROM SSD_SECURITY.GROUPS g " +
            "WHERE g.G_APP = ?1 " +
            "AND g.DATA_CAN IS NULL " +
            "AND SSD_SECURITY.IS_RUOLO_APPLICATIVO_ASSEGNABILE(?2, g.G_APP, g.G_NAME) > 0", nativeQuery = true)
    List<Groups> findAllAssegnabili(String appId, String codiceUtente);

    @Query(value = "SELECT DISTINCT g.* " +
            "FROM SSD_SECURITY.GROUPS g " +
            "WHERE g.G_NAME LIKE %?1% " +
            "AND g.G_NAME NOT IN ?2 " +
            "AND g.G_TYPE != 'APP' " +
            "AND g.DATA_CAN IS NULL " +
            "ORDER BY g.G_NAME ASC", nativeQuery = true)
    List<Groups> findAllByAutocomplete(String parametroRicerca, List<String> ruoliDaEscludere);

    @Query("SELECT g FROM Groups g " +
            "WHERE g.app = ?1 AND g.nome = ?2")
    Groups findByAppIdAndName(String appId, String nome);

    @Query("FROM Groups g WHERE g.tipo = 'APP' AND g.app = ?1 AND g.dataCancellazione IS NULL")
    Groups findMasterByApplicazione(String idApp);

    @Query(value = "SELECT * FROM SSD_SECURITY.GROUPS g " +
            "WHERE g.G_APP = ?1 " +
            "AND g.G_TYPE != 'APP' " +
            "AND NOT EXISTS(SELECT * FROM SSD_SECURITY.GROUPS_AGGREG ga WHERE ga.G_APP_DIP = ?1 AND ga.G_NAME_DIP = g.G_NAME AND ga.DATA_CAN IS NULL) " +
            "AND g.DATA_CAN IS NULL " +
            "ORDER BY g.G_NAME ASC", nativeQuery = true)
    List<Groups> getRuoliPrincipaliAggregazione(String appId);

    @Query(value = "SELECT * FROM SSD_SECURITY.GROUPS g " +
            "WHERE g.G_APP = ?1 " +
            "AND g.G_TYPE != 'APP' " +
            "AND NOT EXISTS(SELECT * FROM SSD_SECURITY.GROUPS_AGGREG ga WHERE ga.G_APP_PRINC = ?1 AND ga.G_NAME_PRINC = g.G_NAME AND ga.DATA_CAN IS NULL) " +
            "AND (g.G_APP != ?2 OR g.G_NAME != ?3) " +
            "AND g.DATA_CAN IS NULL " +
            "ORDER BY g.G_NAME ASC", nativeQuery = true)
    List<Groups> getRuoliDipendentiAggregazione(String appId, String appIdPrincipale, String ruoloPrincipale);


    @Query(value = "SELECT g.* " +
            "FROM SSD_SECURITY.SEC_APPLICAZIONE sa INNER JOIN GROUPS g ON sa.APP_ID = g.G_APP " +
            "WHERE g.DATA_CAN IS NULL " +
            "AND sa.DATE_CANC IS NULL " +
            "AND sa.DATA_INIZIO_OPERATIVITA <= CURRENT_DATE " +
            "AND (sa.DATA_FINE_OPERATIVITA IS NULL OR sa.DATA_FINE_OPERATIVITA >= CURRENT_DATE) " +
            "AND g.G_TYPE != 'APP' " +
            "AND ((?1 = 0 AND UPPER(sa.APP_NAME) LIKE %?2%) OR (?1 = 1 AND UPPER(g.G_NAME) LIKE %?2%)) " +
            "ORDER BY sa.DATE_INS, g.DATE_INS, g.G_NAME DESC",
            countQuery = "SELECT g.* " +
                    "FROM SSD_SECURITY.SEC_APPLICAZIONE sa INNER JOIN GROUPS g ON sa.APP_ID = g.G_APP " +
                    "WHERE g.DATA_CAN IS NULL " +
                    "AND sa.DATE_CANC IS NULL " +
                    "AND sa.DATA_INIZIO_OPERATIVITA <= CURRENT_DATE " +
                    "AND (sa.DATA_FINE_OPERATIVITA IS NULL OR sa.DATA_FINE_OPERATIVITA >= CURRENT_DATE) " +
                    "AND g.G_TYPE != 'APP' " +
                    "AND ((?1 = 0 AND UPPER(sa.APP_NAME) LIKE %?2%) OR (?1 = 1 AND UPPER(g.G_NAME) LIKE %?2%)) " +
                    "ORDER BY sa.DATE_INS, g.DATE_INS, g.G_NAME DESC",
            nativeQuery = true)
    Page<Groups> searchAndPaginateRegoleSicurezza(int flagRicerca, String parametroRicerca, Pageable pageable);

    @Query("FROM Groups " +
            "WHERE nome = ?1 " +
            "AND dataCancellazione IS NULL")
    List<Groups> findByNomeRuolo(String nomeRuolo);

    @Modifying
    @Query(value = "UPDATE SSD_SECURITY.GROUPS " +
            "SET G_NAME = ?2, UTE_AGG = ?3, UFF_AGG = ?4, DATA_AGG = ?5 " +
            "WHERE G_NAME = ?1 AND DATA_CAN IS NULL", nativeQuery = true)
    void updateMaster(String nomeRuoloVecchio, String nomeRuoloNuovo, String utente, String ufficio, Timestamp data);
}


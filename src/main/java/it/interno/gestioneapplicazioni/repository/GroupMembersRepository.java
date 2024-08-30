package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.GroupMembers;
import it.interno.gestioneapplicazioni.entity.key.GroupMembersKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface GroupMembersRepository extends JpaRepository<GroupMembers, GroupMembersKey> {

    @Query("FROM GroupMembers g WHERE TRIM(g.nomeUtente) = TRIM(?1) AND g.nomeRuolo = ?2 AND g.appId = ?3 AND g.dataCancellazione IS NULL")
    GroupMembers getByUtenteAndRuolo(String codiceUtente, String codiceRuolo, String idApplicazione);

    @Query("FROM GroupMembers g WHERE g.nomeRuolo = ?1 AND g.appId = ?2 AND g.dataCancellazione IS NULL")
    List<GroupMembers> getByRuolo(String codiceRuolo, String idApplicazione);

    @Query("FROM GroupMembers g WHERE g.appId = ?1 AND g.dataCancellazione IS NULL")
    List<GroupMembers> getByIdApplicazione(String idApplicazione);

    @Query("FROM GroupMembers g WHERE TRIM(g.nomeUtente) = TRIM(?1) AND g.dataCancellazione IS NULL")
    List<GroupMembers> getByUtente(String codiceUtente);

    /*
    * QUESTA QUERY VIENE UTILIZZATA PER CONTROLLARE SE IN FASE DI DISASSOCIAMENTO DI UN RUOLO, I SUOI DIPENDENTI HANNO
    * ALTRI PADRI CHE SONO ANCH'ESSI ASSOCIATI
    * */
    @Query(value = "SELECT g.* " +
            "FROM SSD_SECURITY.GROUPMEMBERS g, SSD_SECURITY.GROUPS_AGGREG ga " +
            "WHERE ga.G_NAME_DIP = ?3 AND ga.G_APP_DIP = ?4 " +
            "AND ga.G_NAME_PRINC != ?1 AND ga.G_APP_PRINC != ?2 " +
            "AND ga.DATA_CAN IS NULL " +
            "AND g.G_NAME = ga.G_NAME_PRINC AND g.APP_ID = ga.G_APP_PRINC " +
            "AND g.DATA_CAN IS NULL " +
            "AND g.G_MEMBER = ?5", nativeQuery = true)
    List<GroupMembers> getRuoliPrincipaliAssociatiByRuoloDipendente(String nomeRuoloDaEliminare, String idAppDaEliminare, String nomeRuoloDipendente, String idAppDipendente, String codiceUtente);

    @Query(value = "SELECT gm.* " +
            "FROM SSD_SECURITY.GROUPMEMBERS gm INNER JOIN SSD_SECURITY.GROUPS g ON gm.G_NAME = g.G_NAME AND gm.APP_ID = g.G_APP " +
            "WHERE g.G_TYPE != 'APP' " +
            "AND gm.APP_ID = ?1 " +
            "AND gm.G_MEMBER = ?2 " +
            "AND gm.DATA_CAN IS NULL " +
            "AND g.DATA_CAN IS NULL", nativeQuery = true)
    List<GroupMembers> getRuoliNonMasterByAppAndUtente(String idApp, String codiceUtente);

    @Modifying
    @Query(value = "UPDATE SSD_SECURITY.GROUPMEMBERS g " +
            "SET g.UTE_CAN = ?1, g.UFF_CAN = ?2, g.DATA_CAN = ?3 " +
            "WHERE g.G_MEMBER = ?4 " +
            "AND g.G_NAME = ?5 " +
            "AND g.APP_ID = ?6 " +
            "AND g.DATA_CAN IS NULL", nativeQuery = true)
    void deleteByRuoloAndUtente(String utenteOperatore, String ufficioOperatore, Timestamp dataOperazione, String codiceUtente, String nomeRuolo, String idApp);

    @Modifying
    @Query(value = "UPDATE SSD_SECURITY.GROUPMEMBERS gm " +
            "SET gm.UTE_CAN = ?1, gm.UFF_CAN = ?2, gm.DATA_CAN = ?3 " +
            "WHERE gm.G_MEMBER = ?4 " +
            "AND gm.G_NAME = (SELECT g.G_NAME FROM SSD_SECURITY.GROUPS g WHERE g.G_TYPE = 'APP' AND g.G_APP = ?5 AND g.DATA_CAN IS NULL) " +
            "AND gm.APP_ID = ?5 " +
            "AND gm.DATA_CAN IS NULL", nativeQuery = true)
    void deleteMasterByAppAndUtente(String utenteOperatore, String ufficioOperatore, Timestamp dataOperazione, String codiceUtente, String idApp);

    @Query(value =
            "SELECT DISTINCT G_MEMBER " +
            "FROM SSD_SECURITY.GROUPMEMBERS " +
            "WHERE " +
            "     APP_ID = ?1 " +
            " AND DATA_CAN IS NULL", nativeQuery = true)
    List<String> getUtentiDistintiByApp(String idApp);

    @Query(value = "SELECT gm.* " +
            "FROM SSD_SECURITY.GROUPMEMBERS gm INNER JOIN SSD_SECURITY.GROUPS g ON gm.G_NAME = g.G_NAME AND gm.APP_ID = g.G_APP " +
            "WHERE gm.APP_ID = ?1 " +
            "AND gm.G_MEMBER = ?2  AND G_NAME = ?3" +
            "AND gm.DATA_CAN IS NULL " +
            "AND g.DATA_CAN IS NULL", nativeQuery = true)
    List<GroupMembers> getRuoliByAppAndUtenteAndCodiceRuolo(String idApp, String codiceUtente,String codiceRuolo);

    @Query(value =
            "SELECT COUNT(*)  " +
                    "FROM SSD_SECURITY.GROUPMEMBERS " +
                    "WHERE " +
                    "     APP_ID = ?1  AND G_NAME = ?2" +
                    " AND DATA_CAN IS NULL", nativeQuery = true)
    Integer count(String idApp, String codiceRuolo);


    @Query(value = "SELECT g.* FROM SSD_SECURITY.GROUPMEMBERS g  WHERE TRIM(g.G_MEMBER) = TRIM(?1)  AND g.APP_ID = ?2   AND G_NAME NOT IN (?3)  AND g.DATA_CAN IS NULL", nativeQuery = true)
    List<GroupMembers> getByUtenteAndAppNotInCodiceRuolo(String codiceUtente, String idApplicazione, String... codiceRuolo);



}

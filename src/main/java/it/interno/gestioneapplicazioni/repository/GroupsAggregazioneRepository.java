package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.GroupsAggregazione;
import it.interno.gestioneapplicazioni.entity.key.GroupsAggregazioneKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupsAggregazioneRepository extends JpaRepository<GroupsAggregazione, GroupsAggregazioneKey> {

    @Query(value = "SELECT g.* " +
            "FROM SSD_SECURITY.GROUPS_AGGREG g INNER JOIN SSD_SECURITY.SEC_APPLICAZIONE a ON g.G_APP_PRINC = a.APP_ID " +
            "WHERE g.DATA_CAN IS NULL",
            countQuery = "SELECT g.* " +
                    "FROM SSD_SECURITY.GROUPS_AGGREG g INNER JOIN SSD_SECURITY.SEC_APPLICAZIONE a ON g.G_APP_PRINC = a.APP_ID " +
                    "WHERE g.DATA_CAN IS NULL", nativeQuery = true)
    Page<GroupsAggregazione> getListaRuoliAggregatiOrderByRuolo(Pageable pageable);

    @Query(value = "SELECT g.* " +
            "FROM SSD_SECURITY.SEC_APPLICAZIONE a INNER JOIN SSD_SECURITY.GROUPS_AGGREG g ON g.G_APP_PRINC = a.APP_ID " +
            "WHERE g.DATA_CAN IS NULL",
            countQuery = "SELECT g.* " +
                    "FROM SSD_SECURITY.SEC_APPLICAZIONE a INNER JOIN SSD_SECURITY.GROUPS_AGGREG g ON g.G_APP_PRINC = a.APP_ID " +
                    "WHERE g.DATA_CAN IS NULL", nativeQuery = true)
    Page<GroupsAggregazione> getListaRuoliAggregatiOrderByApplicazione(Pageable pageable);

    @Query("FROM GroupsAggregazione WHERE idAppPrincipale = ?1 AND ruoloPrincipale = ?2 AND dataCancellazione IS NULL")
    List<GroupsAggregazione> getAggregazioneByPrincipale(String appPrincipale, String ruoloPrincipale);

    @Query(value = 
            "SELECT * " +
            "FROM " +
            "    SSD_SECURITY.GROUPS_AGGREG " +
            "WHERE " +
            "    G_APP_PRINC = ?1 " +
            "AND G_NAME_PRINC = ?2 " +
            "AND DATA_CAN IS NULL " +
            "UNION " +
            "SELECT A.G_NAME_PRINC, A.G_APP_PRINC, B.G_NAME AS G_NAME_DIP, A.G_APP_DIP, " +
            "       A.DATE_INS, A.UTE_INS, A.UFF_INS, A.DATA_AGG, A.UTE_AGG, A.UFF_AGG, " +
            "       A.DATA_CAN, A.UTE_CAN, A.UFF_CAN " +
            "FROM SSD_SECURITY.GROUPS_AGGREG A, " +
            "     SSD_SECURITY.GROUPS B " +
            "WHERE " +
            "     A.G_APP_PRINC = ?1 " +
            " AND A.G_NAME_PRINC = ?2 " +
            " AND A.DATA_CAN IS NULL " +
            " AND B.G_APP = A.G_APP_DIP " +
            " AND TRIM(B.G_GROUP) = 'G_APP' " +
            " AND B.DATA_CAN IS NULL", nativeQuery = true)
    List<GroupsAggregazione> getAggregazioneAndRuoloMasterByPrincipale(String appPrincipale, String ruoloPrincipale);

    @Query("FROM GroupsAggregazione " +
            "WHERE idAppPrincipale = ?1 " +
            "AND ruoloPrincipale = ?2 " +
            "AND idAppDipendente = ?3 " +
            "AND ruoloDipendente = ?4 " +
            "AND dataCancellazione IS NULL")
    GroupsAggregazione getDettaglioAggregazione(String appPrincipale, String ruoloPrincipale, String appDipendente, String ruoloDipendente);

    @Query(value = "SELECT g.* " +
            "FROM  SSD_SECURITY.GROUPS_AGGREG g  WHERE ( TRIM(g.G_NAME_PRINC) =?1 OR TRIM(g.G_NAME_DIP)= ?1)  AND g.DATA_CAN IS NULL",
           nativeQuery = true)
    List<GroupsAggregazione> getAggregazioneByPrincipaleOrSecondaria(String ruoloPrincipale);

}

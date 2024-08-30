package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.ApplicMotivMembers;
import it.interno.gestioneapplicazioni.entity.key.ApplicMotivMembersKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicMotivMembersRepository extends JpaRepository<ApplicMotivMembers, ApplicMotivMembersKey> {

    @Query("FROM ApplicMotivMembers a " +
            "WHERE a.codiceUtente = ?1 " +
            "AND a.appId = ?2 " +
            "AND a.idTipoMotivazione = ?3 " +
            "AND a.dataCancellazione IS NULL")
    ApplicMotivMembers getByUtenteAppETipoMotivazione(String codiceUtente, String appId, Integer idTipoMotivazione);

    @Query("FROM ApplicMotivMembers a " +
            "WHERE a.codiceUtente = ?1 " +
            "AND a.appId = ?2 " +
            "AND a.dataCancellazione IS NULL")
    List<ApplicMotivMembers> getByUtenteEApp(String codiceUtente, String appId);

    @Query("FROM ApplicMotivMembers a " +
            "WHERE a.appId = ?1 " +
            "AND a.dataCancellazione IS NULL")
    List<ApplicMotivMembers> getByApp(String appId);

}

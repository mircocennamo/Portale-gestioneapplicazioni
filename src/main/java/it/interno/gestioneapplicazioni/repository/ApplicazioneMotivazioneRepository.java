package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.ApplicazioneMotivazione;
import it.interno.gestioneapplicazioni.entity.key.ApplicazioneMotivazioneKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicazioneMotivazioneRepository extends JpaRepository<ApplicazioneMotivazione, ApplicazioneMotivazioneKey> {

    @Query("FROM ApplicazioneMotivazione a WHERE a.idApp = ?1 AND a.dataCancellazione IS NULL")
    List<ApplicazioneMotivazione> findByIdApp(String idApp);



}

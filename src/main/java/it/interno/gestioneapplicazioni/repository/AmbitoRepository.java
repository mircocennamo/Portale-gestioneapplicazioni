package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.Ambito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AmbitoRepository extends JpaRepository<Ambito, String> {

    @Query(value = "SELECT * FROM SSD_SECURITY.SEC_AMBITO_APPLICAZIONE s ORDER BY s.ORDER_ID ASC", nativeQuery = true)
    List<Ambito> getAll();

    @Query("FROM Ambito a WHERE a.idOrdinamento = ?1")
    Ambito getAmbitoByOrderId(Integer idOrdinamento);
}

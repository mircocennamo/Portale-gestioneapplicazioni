package it.interno.gestioneapplicazioni.repository;

import it.interno.gestioneapplicazioni.entity.Gruppo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GruppoRepository extends JpaRepository<Gruppo, Integer> {

    @Query("FROM Gruppo g WHERE g.tipo = ?1")
    List<Gruppo> getAllByType(String type);

}

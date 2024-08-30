package it.interno.gestioneapplicazioni.entity;

import it.interno.gestioneapplicazioni.entity.key.GruppoLavoroKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "SEC_GRUPPO_DI_LAVORO", schema = "SSD_SECURITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GruppoLavoroKey.class)
public class GruppoLavoro {

    @Id
    @Column(name = "ID_GRUPPO_DI_LAVORO")
    private Integer idGruppoLavoro;
    @Id
    @Column(name = "COD_UFF")
    private String codiceUfficio;
    @Column(name = "GRUPPO_DI_LAVORO")
    private String descrizione;
}

package it.interno.gestioneapplicazioni.entity;

import it.interno.gestioneapplicazioni.entity.key.RuoloQualificaAssegnabilitaKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "SEC_RUOLO_QUALIF_ASSEGNABILITA", schema = "SSD_SECURITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RuoloQualificaAssegnabilitaKey.class)
public class RuoloQualificaAssegnabilita {

    @Id
    @Column(name = "G_NAME")
    private String name;
    @Id
    @Column(name = "G_APP")
    private String app;
    @Id
    @Column(name = "QUALIFICA_ASSEGNABILITA_ID")
    private Integer idQualificaAssegnabilita;

}

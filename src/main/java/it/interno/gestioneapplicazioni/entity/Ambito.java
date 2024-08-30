package it.interno.gestioneapplicazioni.entity;

import it.interno.gestioneapplicazioni.entity.key.AmbitoKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "SEC_AMBITO_APPLICAZIONE", schema = "SSD_SECURITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AmbitoKey.class)
public class Ambito {

    @Id
    @Column(name = "AMBITO")
    private String descrizioneAmbito;
    @Column(name = "ICON")
    private String icona;
    @Id
    @Column(name = "ORDER_ID")
    private Integer idOrdinamento;

}

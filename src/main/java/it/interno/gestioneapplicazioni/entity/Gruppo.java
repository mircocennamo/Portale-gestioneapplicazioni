package it.interno.gestioneapplicazioni.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SEC_GRUPPO", schema = "SSD_SECURITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gruppo {

    @Id
    @Column(name = "G_NAME")
    private String nome;

    @Column(name = "G_ID")
    private Integer id;
    @Column(name = "G_DESCR")
    private String descrizione;
    @Column(name = "TYPE")
    private String tipo;
    @Column(name = "G_DESCR_LONG")
    private String longDescription;

}

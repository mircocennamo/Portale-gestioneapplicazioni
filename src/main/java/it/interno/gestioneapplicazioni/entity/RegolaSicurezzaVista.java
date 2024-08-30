package it.interno.gestioneapplicazioni.entity;

import it.interno.gestioneapplicazioni.entity.key.RegolaSicurezzaVistaKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "V_SEC_REGOLE_SICUREZZA", schema = "SSD_SECURITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RegolaSicurezzaVistaKey.class)
public class RegolaSicurezzaVista implements Serializable {

    @Id
    @Column(name = "G_NAME")
    private String nomeRuolo;
    @Id
    @Column(name = "APP_ID")
    private String appId;
    @Id
    @Column(name = "ID_BLOCCO_REGOLA")
    private Integer idRegola;
    @Id
    @Column(name = "ID_PROGRESSIVO_REGOLA")
    private Integer idProgressivoRegola;

    @Column(name = "ID_GRUPPO_DI_LAVORO")
    private Integer idGruppoDiLavoro;
    @Column(name = "GRUPPO_DI_LAVORO")
    private String gruppoDiLavoro;
    @Column(name = "ID_ENTE")
    private Integer idEnte;
    @Column(name = "ENTE")
    private String ente;
    @Column(name = "REGIONE")
    private String regione;
    @Column(name = "PROVINCIA")
    private String provincia;
    @Column(name = "COMUNE")
    private String comune;
    @Column(name = "COD_UFF")
    private String codiceUfficio;
    @Column(name = "UFFICIO")
    private String ufficio;
    @Column(name = "ID_FUNZIONE")
    private String idFunzione;
    @Column(name = "FUNZIONE")
    private String funzione;
    @Column(name = "RUOLO")
    private String ruoloQualifica;
    @Column(name = "NOME_REGOLA_SICUREZZA")
    private String nomeRegola;
    @Column(name = "TIPO_REGOLA")
    private String tipoRegola;
}

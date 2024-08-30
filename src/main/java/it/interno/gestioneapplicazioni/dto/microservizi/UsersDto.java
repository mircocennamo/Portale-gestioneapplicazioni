package it.interno.gestioneapplicazioni.dto.microservizi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersDto {
    private String codiceUtente;
    private String provincia;
    private String codiceRegione;
    private String codiceProvincia;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private Character sesso;
    private String luogoNascita;
    private String codiceFiscale;
    private String email;
    private String telefono;
    private String ufficio;
    private Integer qualifica;
    private String emailPrivata;
    private String utenteInserimento;
    private Timestamp dataInsertimento;
    private String utenteAggiornamento;
    private Timestamp dataAggiornamento;
    private Integer emailPrimaria;
    private String regione;
    private String ripartizione;
    private String siglaProvinciaNacita;
    private Integer idTipoUtente;

    private QualificaProfiloDto qualificaProfilo;
    private ForzaPoliziaDto forzaPolizia;
    private UfficioDto codiceUfficio;
}

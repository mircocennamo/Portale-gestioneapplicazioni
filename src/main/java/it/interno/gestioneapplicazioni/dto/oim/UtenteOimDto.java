package it.interno.gestioneapplicazioni.dto.oim;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.interno.gestioneapplicazioni.serializer.LocalDateDeserializer;
import it.interno.gestioneapplicazioni.serializer.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UtenteOimDto {
    private String codiceUtente;
    private String cognome;
    private String nome;
    private String email;
    private String codiceUfficio;
    private String descrizioneUfficio;
    private String telefono;
    private String descrizioneQualifica;
    private String categoria;
    private String descrizioneEnte;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataNascita;
    private String luogoNascita;
    private String sesso;
    private String codiceFiscale;
    private String emailSecondaria;
}

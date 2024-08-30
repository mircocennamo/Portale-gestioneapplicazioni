package it.interno.gestioneapplicazioni.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.interno.gestioneapplicazioni.annotation.CheckRuoliApplicativiDateValidita;
import it.interno.gestioneapplicazioni.annotation.CheckRuoloMasterNomeApplicazioneValidita;
import it.interno.gestioneapplicazioni.serializer.LocalDateDeserializer;
import it.interno.gestioneapplicazioni.serializer.LocalDateSerializer;
import it.interno.gestioneapplicazioni.serializer.TimestampDeserializer;
import it.interno.gestioneapplicazioni.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CheckRuoliApplicativiDateValidita
@CheckRuoloMasterNomeApplicazioneValidita
public class GroupsDto {

    @NotBlank(message = "Il nome del ruolo è obbligatorio")
    @Size(max = 128, message = "La lunghezza massima per il nome è di 128 caratteri")
    private String nome;

    @NotBlank(message = "L'id dell'applicazione è obbligatorio")
    private String app;

    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataInserimento;

    @NotBlank(message = "La descrizione del ruolo è obbligatoria")
    @Size(max = 500, message = "La lunghezza massima per la descrizione è di 500 caratteri")
    private String descrizione;

    private String gruppo;

    private Integer idGruppo;

    private String tipoGruppo;

    private String utenteInserimento;

    private String ufficioInserimento;

    private String utenteAggiornamento;

    private String ufficioAggiornamento;

    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataAggiornamento;

    private String utenteCancellazione;

    private String ufficioCancellazione;

    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataCancellazione;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "La data inizio validità del ruolo è obbligatoria")
    private LocalDate dataInizioValidita;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataFineValidita;

    private List<Integer> qualificheAssegnate;

    private Boolean regoleSicurezza;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupsDto groupsDto = (GroupsDto) o;
        return getNome().equals(groupsDto.getNome()) && getApp().equals(groupsDto.getApp()) && getDescrizione().equals(groupsDto.getDescrizione()) && Objects.equals(getGruppo(), groupsDto.getGruppo()) && getDataInizioValidita().equals(groupsDto.getDataInizioValidita()) && Objects.equals(getDataFineValidita(), groupsDto.getDataFineValidita()) && Objects.equals(getQualificheAssegnate(), groupsDto.getQualificheAssegnate());
    }

    // EQUALS TRAMITE ID
    public boolean equals(String nome, String app){
        return nome.equals(this.nome) && app.equals(this.app);
    }

    public boolean equalsSenzaQualifiche(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupsDto groupsDto = (GroupsDto) o;
        return getNome().equals(groupsDto.getNome()) && getApp().equals(groupsDto.getApp()) && getDescrizione().equals(groupsDto.getDescrizione()) && Objects.equals(getGruppo(), groupsDto.getGruppo()) && getDataInizioValidita().equals(groupsDto.getDataInizioValidita()) && Objects.equals(getDataFineValidita(), groupsDto.getDataFineValidita());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNome(), getApp(), getDescrizione(), getGruppo(), getDataInizioValidita(), getDataFineValidita(), getQualificheAssegnate());
    }
}

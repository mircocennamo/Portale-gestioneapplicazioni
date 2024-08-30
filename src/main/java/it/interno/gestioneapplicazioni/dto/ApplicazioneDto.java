package it.interno.gestioneapplicazioni.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.interno.gestioneapplicazioni.annotation.CheckAppNameAlreadyExists;
import it.interno.gestioneapplicazioni.annotation.CheckApplicazioneDateOperativitaValidita;
import it.interno.gestioneapplicazioni.annotation.CheckApplicazioneURLAlreadyExists;
import it.interno.gestioneapplicazioni.annotation.CheckApplicazioneUrlValidita;
import it.interno.gestioneapplicazioni.serializer.LocalDateDeserializer;
import it.interno.gestioneapplicazioni.serializer.LocalDateSerializer;
import it.interno.gestioneapplicazioni.serializer.TimestampDeserializer;
import it.interno.gestioneapplicazioni.serializer.TimestampSerializer;
import it.interno.gestioneapplicazioni.validation.ValidazioneAggiornamento;
import it.interno.gestioneapplicazioni.validation.ValidazioneInserimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CheckApplicazioneDateOperativitaValidita
public class ApplicazioneDto {

    @NotBlank(message = "L'id deve essere valorizzato", groups = {ValidazioneAggiornamento.class})
    private String appId;

    @NotBlank(message = "Il nome dell'applicazione deve essere valorizzato", groups = {ValidazioneInserimento.class, ValidazioneAggiornamento.class})
    @CheckAppNameAlreadyExists(groups = {ValidazioneInserimento.class})
    @Size(max = 128, message = "La lunghezza massima per il nome è di 128 caratteri")
    @Pattern(regexp = "^.*[A-Z]+.*$", message = "Il nome deve contenere almeno un carattere")
    private String appName;

    @NotBlank(message = "La descrizione deve essere valorizzata", groups = {ValidazioneInserimento.class, ValidazioneAggiornamento.class})
    @Size(max = 1024, message = "La lunghezza massima per la descrizione è di 1024 caratteri")
    private String appDescription;

    private String appScope;

    @NotBlank(message = "L'URL deve essere valorizzato", groups = {ValidazioneInserimento.class, ValidazioneAggiornamento.class})
    @CheckApplicazioneURLAlreadyExists(groups = {ValidazioneInserimento.class})
    @CheckApplicazioneUrlValidita
    @Size(max = 1024, message = "La lunghezza massima per l'url è di 1024 caratteri")
    private String appUrl;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "La data ini deve essere valorizzata", groups = {ValidazioneAggiornamento.class})
    private LocalDate appDataIni;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "La data fin deve essere valorizzata", groups = {ValidazioneAggiornamento.class})
    private LocalDate appDataFin;

    private String utenteInserimento;

    private String ufficioInserimento;

    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataInserimento;

    private String utenteAggiornamento;

    private String ufficioAggiornamento;

    private Timestamp dataAggiornamento;

    private String utenteCancellazione;

    private String ufficioCancellazione;

    private Timestamp dataCancellazione;

    @NotNull(message = "L'ordine catalogo deve essere valorizzata", groups = {ValidazioneAggiornamento.class})
    private Double idOrdineCatalogo;

    @NotNull(message = "L'id dell'ordine dell'ambito deve essere valorizzato", groups = {ValidazioneAggiornamento.class, ValidazioneInserimento.class})
    private Integer idOrdineAmbito;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "La data di inizio operatività deve essere valorizzata", groups = {ValidazioneInserimento.class, ValidazioneAggiornamento.class})
    private LocalDate dataInizioOperativita;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataFineOperativita;

    @NotNull(message = "La scadenza accesso deve essere valorizzata", groups = {ValidazioneAggiornamento.class, ValidazioneInserimento.class})
    @Min(value = 0, message = "La scadenza accesso deve essere maggiore o uguale a 0", groups = {ValidazioneAggiornamento.class, ValidazioneInserimento.class})
    private Integer scadenzaAccesso;

    @NotNull(message = "La visibilità portale deve essere valorizzata", groups = {ValidazioneInserimento.class, ValidazioneAggiornamento.class})
    private Character visibilitaPortale;

    @NotNull(message = "La visibilità catalogo deve essere valorizzata", groups = {ValidazioneInserimento.class, ValidazioneAggiornamento.class})
    private Character visibilitaCatalogo;

    private List<@NotNull Integer> idMotivazione;

    private List<GroupsDto> ruoliApplicativi;
}

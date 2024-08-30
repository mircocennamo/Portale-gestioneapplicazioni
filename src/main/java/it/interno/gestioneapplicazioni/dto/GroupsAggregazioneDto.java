package it.interno.gestioneapplicazioni.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.interno.gestioneapplicazioni.serializer.TimestampDeserializer;
import it.interno.gestioneapplicazioni.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupsAggregazioneDto {
    private String ruoloPrincipale;
    private String idAppPrincipale;
    private String ruoloDipendente;
    private String idAppDipendente;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataInserimento;
    private String utenteInserimento;
    private String ufficioInserimento;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataCancellazione;
    private String utenteCancellazione;
    private String ufficioCancellazione;

    private String nomeApplicazionePrincipale;
}

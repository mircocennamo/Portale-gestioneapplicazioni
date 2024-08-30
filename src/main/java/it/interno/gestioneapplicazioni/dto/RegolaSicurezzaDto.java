package it.interno.gestioneapplicazioni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegolaSicurezzaDto {

    private String ruolo;
    private String appId;
    private Integer numeroRegola;
    private String nomeRegola;
    private String tipoRegola;
    private boolean isEdit;

    private List<IdEDescrizioneDto<Integer>> gruppiLavoro = new ArrayList<>();
    private List<IdEDescrizioneDto<Integer>> enti = new ArrayList<>();
    private List<String> regioni = new ArrayList<>();
    private List<String> province = new ArrayList<>();
    private List<String> comuni = new ArrayList<>();
    private List<IdEDescrizioneDto<String>> uffici = new ArrayList<>();
    private List<IdEDescrizioneDto<String>> funzioni = new ArrayList<>();
    private List<String> ruoliQualifica = new ArrayList<>();

    public RegolaSicurezzaDto(String ruolo, String appId, Integer numeroRegola, String nomeRegola, String tipoRegola) {
        this.ruolo = ruolo;
        this.appId = appId;
        this.numeroRegola = numeroRegola;
        this.nomeRegola = nomeRegola;
        this.tipoRegola = tipoRegola;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegolaSicurezzaDto that = (RegolaSicurezzaDto) o;
        return getRuolo().equals(that.getRuolo()) && getAppId().equals(that.getAppId()) && Objects.equals(getNumeroRegola(), that.getNumeroRegola());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRuolo(), getAppId(), getNumeroRegola());
    }
}

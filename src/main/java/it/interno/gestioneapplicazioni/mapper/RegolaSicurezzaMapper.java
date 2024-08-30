package it.interno.gestioneapplicazioni.mapper;

import it.interno.gestioneapplicazioni.dto.IdEDescrizioneDto;
import it.interno.gestioneapplicazioni.dto.RegolaSicurezzaDto;
import it.interno.gestioneapplicazioni.entity.RegolaSicurezza;
import it.interno.gestioneapplicazioni.entity.RegolaSicurezzaVista;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface RegolaSicurezzaMapper {

    static List<RegolaSicurezza> toEntity(RegolaSicurezzaDto regolaSicurezzaDto){

        AtomicInteger progressivo = new AtomicInteger(1);
        List<RegolaSicurezza> regoleSicurezza = new ArrayList<>();

        // SE TUTTE LE LISTE SONO VUOTE LA REGOLA E' SOLO UNA CON TUTTI I CAMPI VUOTI
        if(regolaSicurezzaDto.getGruppiLavoro().isEmpty() && regolaSicurezzaDto.getEnti().isEmpty()
                && regolaSicurezzaDto.getRegioni().isEmpty() && regolaSicurezzaDto.getProvince().isEmpty()
                && regolaSicurezzaDto.getComuni().isEmpty() && regolaSicurezzaDto.getUffici().isEmpty()
                && regolaSicurezzaDto.getFunzioni().isEmpty() && regolaSicurezzaDto.getRuoliQualifica().isEmpty()){

            regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), 1, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola()));
            return regoleSicurezza;
        }

        regolaSicurezzaDto.getGruppiLavoro().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), el.getId(), null, null, null, null, null, null, null, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));
        regolaSicurezzaDto.getEnti().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), null, el.getId(), null, null, null, null, null, null, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));
        regolaSicurezzaDto.getRegioni().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), null, null, el, null, null, null, null, null, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));
        regolaSicurezzaDto.getProvince().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), null, null, null, el, null, null, null, null, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));
        regolaSicurezzaDto.getComuni().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), null, null, null, null, el, null, null, null, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));
        regolaSicurezzaDto.getUffici().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), null, null, null, null, null, el.getId(), null, null, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));
        regolaSicurezzaDto.getFunzioni().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), null, null, null, null, null, null, el.getId(), null, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));
        regolaSicurezzaDto.getRuoliQualifica().forEach(el -> regoleSicurezza.add(new RegolaSicurezza(regolaSicurezzaDto.getRuolo(), regolaSicurezzaDto.getAppId(), regolaSicurezzaDto.getNumeroRegola(), progressivo.getAndIncrement(), null, null, null, null, null, null, null, el, regolaSicurezzaDto.getNomeRegola(), regolaSicurezzaDto.getTipoRegola())));

        return regoleSicurezza;
    }

    static RegolaSicurezzaDto toDto(List<RegolaSicurezzaVista> regolaSicurezza){

        if(regolaSicurezza.isEmpty())
            return new RegolaSicurezzaDto();

        RegolaSicurezzaDto regolaSicurezzaDto = new RegolaSicurezzaDto(regolaSicurezza.get(0).getNomeRuolo(), regolaSicurezza.get(0).getAppId(), regolaSicurezza.get(0).getIdRegola(), regolaSicurezza.get(0).getNomeRegola(), regolaSicurezza.get(0).getTipoRegola());

        regolaSicurezza.forEach(el -> {

            if(el.getIdGruppoDiLavoro() != null)
                regolaSicurezzaDto.getGruppiLavoro().add(new IdEDescrizioneDto<>(el.getIdGruppoDiLavoro(), el.getGruppoDiLavoro()));

            if(el.getIdEnte() != null)
                regolaSicurezzaDto.getEnti().add(new IdEDescrizioneDto<>(el.getIdEnte(), el.getEnte()));

            if(!StringUtils.isBlank(el.getRegione()))
                regolaSicurezzaDto.getRegioni().add(el.getRegione());

            if(!StringUtils.isBlank(el.getProvincia()))
                regolaSicurezzaDto.getProvince().add(el.getProvincia());

            if(!StringUtils.isBlank(el.getComune()))
                regolaSicurezzaDto.getComuni().add(el.getComune());

            if(!StringUtils.isBlank(el.getCodiceUfficio()))
                regolaSicurezzaDto.getUffici().add(new IdEDescrizioneDto<>(el.getCodiceUfficio(), el.getUfficio()));

            if(!StringUtils.isBlank(el.getIdFunzione()))
                regolaSicurezzaDto.getFunzioni().add(new IdEDescrizioneDto<>(el.getIdFunzione(), el.getFunzione()));

            if(!StringUtils.isBlank(el.getRuoloQualifica()))
                regolaSicurezzaDto.getRuoliQualifica().add(el.getRuoloQualifica());

        });

        return regolaSicurezzaDto;
    }
}

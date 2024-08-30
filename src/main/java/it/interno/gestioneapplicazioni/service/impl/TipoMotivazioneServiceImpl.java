package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.dto.TipoMotivazioneDto;
import it.interno.gestioneapplicazioni.mapper.TipoMotivazioneMapper;
import it.interno.gestioneapplicazioni.repository.TipoMotivazioneRepository;
import it.interno.gestioneapplicazioni.service.TipoMotivazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoMotivazioneServiceImpl implements TipoMotivazioneService {

    @Autowired
    private TipoMotivazioneRepository tipoMotivazioneRepository;
    @Autowired
    private TipoMotivazioneMapper tipoMotivazioneMapper;

    @Override
    public List<TipoMotivazioneDto> getAllTipiMotivazione() {
        return tipoMotivazioneRepository.getAllTipiMotivazione()
                .stream()
                .map(el -> {
                    el.setDescrizione(el.getDescrizione().trim());
                    return tipoMotivazioneMapper.toDto(el);
                })
                .toList();
    }
}

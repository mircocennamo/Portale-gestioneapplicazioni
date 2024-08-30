package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.dto.GruppoLavoroDto;
import it.interno.gestioneapplicazioni.mapper.GruppoLavoroMapper;
import it.interno.gestioneapplicazioni.repository.GruppoLavoroRepository;
import it.interno.gestioneapplicazioni.service.GruppoLavoroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GruppoLavoroServiceImpl implements GruppoLavoroService {

    @Autowired
    private GruppoLavoroRepository gruppoLavoroRepository;
    @Autowired
    private GruppoLavoroMapper gruppoLavoroMapper;

    @Override
    public List<GruppoLavoroDto> getAllGruppoLavoro() {
        return gruppoLavoroRepository.getAllGruppoLavoro();
    }
}

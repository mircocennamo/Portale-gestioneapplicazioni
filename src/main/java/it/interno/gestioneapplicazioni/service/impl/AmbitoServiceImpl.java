package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.dto.AmbitoDto;
import it.interno.gestioneapplicazioni.mapper.AmbitoMapper;
import it.interno.gestioneapplicazioni.repository.AmbitoRepository;
import it.interno.gestioneapplicazioni.service.AmbitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmbitoServiceImpl implements AmbitoService {

    @Autowired
    private AmbitoRepository ambitoRepository;
    @Autowired
    private AmbitoMapper ambitoMapper;

    @Override
    public List<AmbitoDto> getAll() {
        return ambitoRepository.getAll()
                .stream()
                .map(el -> ambitoMapper.toDto(el))
                .toList();
    }
}

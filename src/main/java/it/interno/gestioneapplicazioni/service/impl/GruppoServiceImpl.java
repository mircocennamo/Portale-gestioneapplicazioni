package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.dto.GruppoDto;
import it.interno.gestioneapplicazioni.mapper.GruppoMapper;
import it.interno.gestioneapplicazioni.repository.GruppoRepository;
import it.interno.gestioneapplicazioni.service.GruppoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GruppoServiceImpl implements GruppoService {

    @Autowired
    private GruppoRepository gruppoRepository;
    @Autowired
    private GruppoMapper gruppoMapper;

    @Override
    public List<GruppoDto> getAllByType(String type) {
        return gruppoRepository.getAllByType(type)
                .stream().map(el -> gruppoMapper.toDto(el))
                .toList();
    }
}

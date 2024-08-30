package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.dto.QualificaAssegnabilitaDto;
import it.interno.gestioneapplicazioni.mapper.QualificaAssegnabilitaMapper;
import it.interno.gestioneapplicazioni.repository.QualificaAssegnabilitaRepository;
import it.interno.gestioneapplicazioni.service.QualificaAssegnabilitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualificaAssegnabilitaServiceImpl implements QualificaAssegnabilitaService {

    @Autowired
    private QualificaAssegnabilitaRepository qualificaAssegnabilitaRepository;
    @Autowired
    private QualificaAssegnabilitaMapper qualificaAssegnabilitaMapper;

    @Override
    public List<QualificaAssegnabilitaDto> getAll() {
        return qualificaAssegnabilitaRepository.findAll()
                .stream().map(el -> qualificaAssegnabilitaMapper.toDto(el))
                .toList();
    }
}

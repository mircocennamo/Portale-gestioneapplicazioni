package it.interno.gestioneapplicazioni.service;

import it.interno.gestioneapplicazioni.dto.GruppoDto;

import java.util.List;

public interface GruppoService {
    List<GruppoDto> getAllByType(String type);
}

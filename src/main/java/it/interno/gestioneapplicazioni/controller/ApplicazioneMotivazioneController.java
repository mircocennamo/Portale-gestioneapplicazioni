package it.interno.gestioneapplicazioni.controller;

import it.interno.gestioneapplicazioni.service.ApplicazioneMotivazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/applicazione-motivazione", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApplicazioneMotivazioneController {

    @Autowired
    private ApplicazioneMotivazioneService applicazioneMotivazioneService;

}

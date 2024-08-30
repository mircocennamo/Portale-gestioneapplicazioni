package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.entity.ApplicazioneMotivazione;
import it.interno.gestioneapplicazioni.repository.ApplicazioneMotivazioneRepository;
import it.interno.gestioneapplicazioni.service.ApplicazioneMotivazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;


@Service
public class ApplicazioneMotivazioneServiceImpl implements ApplicazioneMotivazioneService {

    @Autowired
    private ApplicazioneMotivazioneRepository applicazioneMotivazioneRepository;


    @Override
    public void deleteByAppId(String appId,String utenteCancellazione, String ufficioCancellazione, Timestamp data) {
        List<ApplicazioneMotivazione>  applicazioneMotivazioni = applicazioneMotivazioneRepository.findByIdApp(appId);
        applicazioneMotivazioni.stream().forEach(applicazioneMotivazione -> {
            applicazioneMotivazione.setUtentecancellazione(utenteCancellazione);
            applicazioneMotivazione.setDataCancellazione(data);
            applicazioneMotivazione.setUfficioCancellazione(ufficioCancellazione);
         });
        applicazioneMotivazioneRepository.saveAll(applicazioneMotivazioni);
    }
}

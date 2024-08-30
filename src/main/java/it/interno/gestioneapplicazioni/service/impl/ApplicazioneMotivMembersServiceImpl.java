package it.interno.gestioneapplicazioni.service.impl;

import it.interno.gestioneapplicazioni.entity.ApplicMotivMembers;
import it.interno.gestioneapplicazioni.repository.ApplicMotivMembersRepository;
import it.interno.gestioneapplicazioni.service.ApplicazioneMotivMembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;


@Service
public class ApplicazioneMotivMembersServiceImpl implements ApplicazioneMotivMembersService {

    @Autowired
    private ApplicMotivMembersRepository applicMotivMembersRepository;

    @Override
    public void deleteByAppId(String appId,String utenteCancellazione, String ufficioCancellazione, Timestamp data) {
        List<ApplicMotivMembers>  applicMotivMembers = applicMotivMembersRepository.getByApp(appId);
        applicMotivMembers.stream().forEach(applicMotivMember -> {
            applicMotivMember.setUtenteCancellazione(utenteCancellazione);
            applicMotivMember.setDataCancellazione(data);
            applicMotivMember.setUfficioCancellazione(ufficioCancellazione);
         });
        applicMotivMembersRepository.saveAll(applicMotivMembers);
    }

    @Override
    public void deleteByAppIdAndUtente(String appId, String utente, String utenteCancellazione, String ufficioCancellazione, Timestamp data) {
        List<ApplicMotivMembers>  applicMotivMembers = applicMotivMembersRepository.getByUtenteEApp(utente, appId);
        applicMotivMembers.stream().forEach(applicMotivMember -> {
            applicMotivMember.setUtenteCancellazione(utenteCancellazione);
            applicMotivMember.setDataCancellazione(data);
            applicMotivMember.setUfficioCancellazione(ufficioCancellazione);
        });
        applicMotivMembersRepository.saveAll(applicMotivMembers);
    }

}

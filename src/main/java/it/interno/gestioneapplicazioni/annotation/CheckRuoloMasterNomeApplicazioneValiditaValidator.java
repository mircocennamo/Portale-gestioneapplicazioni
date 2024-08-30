package it.interno.gestioneapplicazioni.annotation;

import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.entity.Applicazione;
import it.interno.gestioneapplicazioni.repository.ApplicazioneRepository;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckRuoloMasterNomeApplicazioneValiditaValidator implements ConstraintValidator<CheckRuoloMasterNomeApplicazioneValidita, GroupsDto> {

    @Autowired
    private ApplicazioneRepository applicazioneRepository;

    @Override
    public void initialize(CheckRuoloMasterNomeApplicazioneValidita constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(GroupsDto ruolo, ConstraintValidatorContext constraintValidatorContext) {

        // Se non è il ruolo master può passare la validazione
        if(StringUtils.isBlank(ruolo.getTipoGruppo()) || !ruolo.getTipoGruppo().equals("APP"))
            return true;

        int daSplittare = ruolo.getApp().length() + 3;
        String nomeApplicazioneInRuolo = ruolo.getNome().substring(daSplittare);
        Applicazione applicazione = applicazioneRepository.findById(ruolo.getApp()).orElse(null);

        return applicazione != null && ConversionUtils.conversioneCaratteriSpeciali(applicazione.getAppName())
                .equalsIgnoreCase(nomeApplicazioneInRuolo);
    }
}

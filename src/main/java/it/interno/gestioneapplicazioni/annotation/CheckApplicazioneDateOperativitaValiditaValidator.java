package it.interno.gestioneapplicazioni.annotation;

import it.interno.gestioneapplicazioni.dto.ApplicazioneDto;
import it.interno.gestioneapplicazioni.entity.Applicazione;
import it.interno.gestioneapplicazioni.exception.ApplicazioneNotFoundException;
import it.interno.gestioneapplicazioni.repository.ApplicazioneRepository;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CheckApplicazioneDateOperativitaValiditaValidator implements ConstraintValidator<CheckApplicazioneDateOperativitaValidita, ApplicazioneDto> {

    @Autowired
    private ApplicazioneRepository repository;

    @Override
    public void initialize(CheckApplicazioneDateOperativitaValidita constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ApplicazioneDto applicazioneDto, ConstraintValidatorContext constraintValidatorContext) {

        // Se l'applicazione non esiste è un inserimento altrimenti un aggiornamento
        if(applicazioneDto.getAppId() == null )
            return !(applicazioneDto.getDataInizioOperativita().isBefore(LocalDate.now())
                    || (applicazioneDto.getDataFineOperativita() != null
                    && applicazioneDto.getDataInizioOperativita().isAfter(applicazioneDto.getDataFineOperativita()))
            );

        Applicazione app = repository.findById(applicazioneDto.getAppId()).orElse(null);

        if(app == null)
            throw new ApplicazioneNotFoundException("L'applicazione " + applicazioneDto.getAppName() + " non è stata trovata");

        return !(applicazioneDto.getDataInizioOperativita().isBefore(ConversionUtils.timestampToLocalDate(app.getDataInserimento()))
                || (applicazioneDto.getDataFineOperativita() != null
                && applicazioneDto.getDataInizioOperativita().isAfter(applicazioneDto.getDataFineOperativita()))
        );
    }
}

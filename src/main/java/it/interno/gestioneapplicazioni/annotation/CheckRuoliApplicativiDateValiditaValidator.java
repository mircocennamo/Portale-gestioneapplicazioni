package it.interno.gestioneapplicazioni.annotation;

import it.interno.gestioneapplicazioni.dto.GroupsDto;
import it.interno.gestioneapplicazioni.entity.Groups;
import it.interno.gestioneapplicazioni.entity.key.GroupsKey;
import it.interno.gestioneapplicazioni.repository.GroupsRepository;
import it.interno.gestioneapplicazioni.utils.ConversionUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;

public class CheckRuoliApplicativiDateValiditaValidator implements ConstraintValidator<CheckRuoliApplicativiDateValidita, GroupsDto> {

    @Autowired
    private GroupsRepository repository;

    @Override
    public void initialize(CheckRuoliApplicativiDateValidita constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(GroupsDto input, ConstraintValidatorContext constraintValidatorContext) {

        // Get Preventiva
        Groups ruoloApplicativo = repository.findById(new GroupsKey(input.getNome(), input.getApp())).orElse(null);

        // Se il ruolo applicativo non esiste è un inserimento altrimenti un aggiornamento
        if(ruoloApplicativo == null)
            return !(input.getDataInizioValidita().isBefore(LocalDate.now())
                    || (input.getDataFineValidita() != null
                    && input.getDataInizioValidita().isAfter(input.getDataFineValidita())));


        return !(input.getDataInizioValidita().isBefore(ConversionUtils.timestampToLocalDate(ruoloApplicativo.getDataInserimento()))
                || (input.getDataFineValidita() != null
                && input.getDataInizioValidita().isAfter(input.getDataFineValidita()))
        );
    }
}

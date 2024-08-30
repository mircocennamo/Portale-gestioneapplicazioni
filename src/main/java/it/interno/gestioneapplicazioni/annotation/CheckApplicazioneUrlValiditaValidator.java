package it.interno.gestioneapplicazioni.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckApplicazioneUrlValiditaValidator implements ConstraintValidator<CheckApplicazioneUrlValidita, String> {

    @Override
    public void initialize(CheckApplicazioneUrlValidita constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        return !url.contains(" ");
    }

}

package it.interno.gestioneapplicazioni.annotation;

import it.interno.gestioneapplicazioni.repository.ApplicazioneRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;



public class CheckApplicazioneURLAlreadyExistsValidator implements ConstraintValidator<CheckApplicazioneURLAlreadyExists, String> {

    @Autowired
    private ApplicazioneRepository repository;

    @Override
    public void initialize(CheckApplicazioneURLAlreadyExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return repository.checkURLAlreadyExists(s).isEmpty();
    }

}

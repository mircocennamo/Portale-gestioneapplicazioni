package it.interno.gestioneapplicazioni.annotation;

import it.interno.gestioneapplicazioni.repository.ApplicazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckAppNameAlreadyExistsValidator implements ConstraintValidator<CheckAppNameAlreadyExists, String> {

    @Autowired
    private ApplicazioneRepository repository;

    @Override
    public void initialize(CheckAppNameAlreadyExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return repository.checkNameAlreadyExists(s).isEmpty();
    }

}

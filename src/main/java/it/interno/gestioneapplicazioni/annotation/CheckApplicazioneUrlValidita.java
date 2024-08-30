package it.interno.gestioneapplicazioni.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CheckApplicazioneUrlValiditaValidator.class)
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface CheckApplicazioneUrlValidita {

    String message() default "Formato URL invalido.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

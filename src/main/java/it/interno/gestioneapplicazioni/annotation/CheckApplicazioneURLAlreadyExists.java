package it.interno.gestioneapplicazioni.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CheckApplicazioneURLAlreadyExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface CheckApplicazioneURLAlreadyExists {

    String message() default "Esiste già un'applicazione con lo stesso URL.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

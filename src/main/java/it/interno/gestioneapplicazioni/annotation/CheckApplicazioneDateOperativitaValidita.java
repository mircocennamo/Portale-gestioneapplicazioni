package it.interno.gestioneapplicazioni.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = CheckApplicazioneDateOperativitaValiditaValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckApplicazioneDateOperativitaValidita {

    String message() default "Errore di validazione per le date operatività.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

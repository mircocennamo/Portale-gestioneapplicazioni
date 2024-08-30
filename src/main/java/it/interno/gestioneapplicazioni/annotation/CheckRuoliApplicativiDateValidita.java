package it.interno.gestioneapplicazioni.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = CheckRuoliApplicativiDateValiditaValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRuoliApplicativiDateValidita {

    String message() default "Errore di validazione per le date validità.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

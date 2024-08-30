package it.interno.gestioneapplicazioni.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = CheckRuoloMasterNomeApplicazioneValiditaValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRuoloMasterNomeApplicazioneValidita {

    String message() default "Incongruenza nel nome del ruolo master. Ricaricare la pagina.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

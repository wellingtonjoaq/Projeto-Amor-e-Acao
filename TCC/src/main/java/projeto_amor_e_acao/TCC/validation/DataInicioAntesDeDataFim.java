package projeto_amor_e_acao.TCC.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataInicioAntesDeDataFimValidator.class)
@Documented
public @interface DataInicioAntesDeDataFim {
    String message() default "Data Início deve ser antes de Data Fim";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

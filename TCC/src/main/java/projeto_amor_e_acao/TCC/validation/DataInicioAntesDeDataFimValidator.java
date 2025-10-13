package projeto_amor_e_acao.TCC.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import projeto_amor_e_acao.TCC.model.Curso;

public class DataInicioAntesDeDataFimValidator implements ConstraintValidator<DataInicioAntesDeDataFim, Curso> {
    @Override
    public boolean isValid(Curso curso, ConstraintValidatorContext context) {
        if (curso == null || curso.getDataInicio() == null || curso.getDataFim() == null) {
            return true;
        }

        if (curso.getDataInicio().isAfter(curso.getDataFim())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("( Campo Invalido )")
                    .addPropertyNode("dataInicio")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("( Campo Invalido )")
                    .addPropertyNode("dataFim")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

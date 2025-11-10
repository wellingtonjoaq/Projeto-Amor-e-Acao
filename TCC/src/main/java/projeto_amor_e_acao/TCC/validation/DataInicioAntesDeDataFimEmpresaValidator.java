package projeto_amor_e_acao.TCC.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;

public class DataInicioAntesDeDataFimEmpresaValidator implements ConstraintValidator<DataInicioAntesDeDataFim, EmpresaParceira> {
    @Override
    public boolean isValid(EmpresaParceira empresaParceira, ConstraintValidatorContext context) {
        if (empresaParceira == null || empresaParceira.getDataInicio() == null || empresaParceira.getDataFim() == null) {
            return true;
        }

        if (empresaParceira.getDataInicio().isAfter(empresaParceira.getDataFim())) {
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

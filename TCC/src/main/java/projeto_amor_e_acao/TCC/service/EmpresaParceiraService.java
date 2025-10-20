package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.repository.EmpresaParceiraRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaParceiraService {

    @Autowired
    private EmpresaParceiraRepository repository;

    public Optional<EmpresaParceira> findByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj);
    }

    public List<EmpresaParceira> findAll() {
        return repository.findByStatusIgnoreCase("ATIVO");
    }

    public List<EmpresaParceira> listarInativos() {
        return repository.findByStatusIgnoreCase("INATIVO");
    }

    public Optional<EmpresaParceira> findById(Long id) {
        return repository.findById(id);
    }

    public EmpresaParceira save(EmpresaParceira empresaParceira) {
        // Verificar duplicidade de CNPJ
        repository.findByCnpj(empresaParceira.getCnpj()).ifPresent(existingEmpresa -> {
            if (empresaParceira.getId() == null ||
                    !existingEmpresa.getId().equals(empresaParceira.getId()))
            {
                throw new IllegalArgumentException(
                        "Já existe uma empresa cadastrada com este CNPJ.");
            }
        });

        //Verificar datas
        if (empresaParceira.getData_fim() != null &&
                empresaParceira.getData_inicio() != null &&
                empresaParceira.getData_fim().isBefore(
                empresaParceira.getData_inicio().toLocalDate()))
        {
            throw new IllegalArgumentException(
                    "A data de fim não pode ser anterior à data de início.");
        }

        return repository.save(empresaParceira);
    }

    public void deleteById(Long id) {
        EmpresaParceira empresa = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        repository.deleteById(id);
    }
}

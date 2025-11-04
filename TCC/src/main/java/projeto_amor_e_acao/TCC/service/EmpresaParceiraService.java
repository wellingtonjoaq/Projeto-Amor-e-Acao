package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
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

    public Page<EmpresaParceira> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public List<EmpresaParceira> listarTodosInativos() {
        return repository.findByStatusIgnoreCase("INATIVO");
    }

    public Page<EmpresaParceira> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Page<EmpresaParceira> listarPendentes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("PENDENTE", pageable);
    }

    public Page<EmpresaParceira> filtrarPesquisa(String pesquisa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (pesquisa == null || pesquisa.isBlank()) {
            return Page.empty(pageable);
        }

        pesquisa = pesquisa.trim();
        Page<EmpresaParceira> resultados = repository.findByStatusIgnoreCaseAndNomeContainingIgnoreCase("ATIVO", pesquisa, pageable);

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCnpjContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndNomeRepresentanteContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCpfRepresentanteContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEmailContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        return resultados;
    }

    public Optional<EmpresaParceira> findById(Long id) {
        return repository.findById(id);
    }

    public EmpresaParceira save(EmpresaParceira empresaParceira) {
        repository.findByCnpj(empresaParceira.getCnpj()).ifPresent(existingEmpresa -> {
            if (empresaParceira.getId() == null ||
                    !existingEmpresa.getId().equals(empresaParceira.getId()))
            {
                throw new IllegalArgumentException(
                        "Já existe uma empresa cadastrada com este CNPJ.");
            }
        });

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

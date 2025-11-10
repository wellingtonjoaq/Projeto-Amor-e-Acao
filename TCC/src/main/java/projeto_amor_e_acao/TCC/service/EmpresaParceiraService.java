package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.repository.EmpresaParceiraRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaParceiraService {

    @Autowired
    private EmpresaParceiraRepository repository;

    @Transactional
    public EmpresaParceira salvar(EmpresaParceira empresaParceira) {
        empresaParceira.setDataAlteracaoStatus(LocalDate.now());

        var existenteCnpj = repository.findByCnpjIgnoreCase(empresaParceira.getCnpj());
        if (existenteCnpj.isPresent() && !existenteCnpj.get().getId().equals(empresaParceira.getId())) {
            throw new IllegalStateException("( Esse CNPJ já existe! )");
        }

        var existenteCpf = repository.findByCpfRepresentanteIgnoreCase(empresaParceira.getCpfRepresentante());
        if (existenteCpf.isPresent() && !existenteCpf.get().getId().equals(empresaParceira.getId())) {
            throw new IllegalStateException("( Esse CPF já existe! )");
        }

        var existenteEmail = repository.findByEmailIgnoreCase(empresaParceira.getEmail());
        if (existenteEmail.isPresent() && !existenteEmail.get().getId().equals(empresaParceira.getId())) {
            throw new IllegalStateException("( Esse E-mail já existe! )");
        }

        try {
            return repository.save(empresaParceira);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar empresa parceira.", e);
        }
    }

    public Page<EmpresaParceira> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public Page<EmpresaParceira> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Page<EmpresaParceira> listarPendentes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("PENDENTE", pageable);
    }

    public Page<EmpresaParceira> filtrarPesquisa(String status, String pesquisa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (pesquisa == null || pesquisa.isBlank()) {
            return Page.empty(pageable);
        }

        pesquisa = pesquisa.trim();
        Page<EmpresaParceira> resultados = repository.findByStatusIgnoreCaseAndNomeContainingIgnoreCase(status, pesquisa, pageable);

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCnpjContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndNomeRepresentanteContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCpfRepresentanteContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEmailContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase(status, pesquisa, pageable);
        }

        return resultados;
    }

    public Optional<EmpresaParceira> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void deletarPorId(Long id) {
        EmpresaParceira empresa = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        repository.deleteById(id);
    }
}

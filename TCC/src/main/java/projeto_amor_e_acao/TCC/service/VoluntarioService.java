package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.repository.VoluntarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

    @Transactional
    public void salvar(Voluntario voluntario) {
        voluntario.setDataAlteracaoStatus(LocalDate.now());

        if (voluntario.getFuncao() != null && voluntario.getFuncao().getId() != null &&
                voluntario.getFuncao().getId() == 0) {
            voluntario.setFuncao(null);
        }

        try {
            repository.save(voluntario);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar voluntario.", e);
        }
    }

    public Page<Voluntario> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public List<Voluntario> listarTodosInativos() {
        return repository.findByStatusIgnoreCase("INATIVO");
    }

    public Page<Voluntario> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Page<Voluntario> listarPendentes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("PENDENT", pageable);
    }

    public Page<Voluntario> filtrarPesquisa(String status, String pesquisa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (pesquisa == null || pesquisa.isBlank()) {
            return Page.empty(pageable);
        }

        pesquisa = pesquisa.trim();
        Page<Voluntario> resultados = repository.findByStatusIgnoreCaseAndNomeContainingIgnoreCase(status, pesquisa, pageable);

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEmailContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCpfContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCepContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndBairroContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCidadeContainingIgnoreCase(status, pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEstadoContainingIgnoreCase(status, pesquisa, pageable);
        }

        return resultados;
    }

    public Page<Voluntario> filtrar(List<FuncaoVoluntario> funcaoVoluntarios, String genero, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        boolean temFuncao = (funcaoVoluntarios != null && !funcaoVoluntarios.isEmpty());
        boolean temGenero = (genero != null && !genero.isBlank() && !genero.equalsIgnoreCase("TODOS"));

        if (temFuncao && temGenero) {
            return repository.findByStatusIgnoreCaseAndFuncaoInAndGeneroIgnoreCase(
                    "ATIVO", funcaoVoluntarios, genero, pageable
            );
        } else if (temFuncao) {
            return repository.findByStatusIgnoreCaseAndFuncaoIn(
                    "ATIVO", funcaoVoluntarios, pageable
            );
        } else if (temGenero) {
            return repository.findByStatusIgnoreCaseAndGeneroIgnoreCase(
                    "ATIVO", genero, pageable
            );
        }

        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public Voluntario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

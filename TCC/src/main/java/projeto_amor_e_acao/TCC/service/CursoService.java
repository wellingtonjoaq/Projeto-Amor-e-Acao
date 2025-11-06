package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.repository.AlunoRepository;
import projeto_amor_e_acao.TCC.repository.CursoRepository;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository repository;

    @Transactional
    public void salvar(Curso curso) {
        try {
            repository.save(curso);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar curso.", e);
        }
    }

    public List<Curso> listarTodos() {
        return repository.findAll();
    }

    public Page<Curso> listarPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public Page<Curso> filtrarPesquisa(String pesquisa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (pesquisa == null || pesquisa.isBlank()) {
            return Page.empty(pageable);
        }

        pesquisa = pesquisa.trim();
        Page<Curso> resultados = repository.findByStatusIgnoreCaseAndNomeContainingIgnoreCase("ATIVO", pesquisa, pageable);

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndProfessorContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCepContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndBairroContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCategoriasContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        return resultados;
    }


    public Page<Curso> filtrar(String categoria, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        boolean temCategoria = (categoria != null && !categoria.isEmpty());
        boolean temStatus = (status != null && !status.isBlank() && !status.equalsIgnoreCase("TODOS"));

        if (temCategoria && temStatus) {
            return repository.findByStatusIgnoreCaseAndCategoriasContainingIgnoreCase(status, categoria, pageable
            );
        } else if (temCategoria) {
            return repository.findByStatusIgnoreCaseAndCategoriasContainingIgnoreCase(
                    "ATIVO", categoria, pageable
            );
        } else if (temStatus) {
            return repository.findByStatusIgnoreCase(
                    status, pageable
            );
        }

        return repository.findAll(pageable);
    }

    public Curso buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Curso> buscarPorIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

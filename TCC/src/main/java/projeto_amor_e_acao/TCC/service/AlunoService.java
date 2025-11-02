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

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    @Transactional
    public Aluno salvar(Aluno aluno) {
        aluno.getMatriculas().forEach(matricula -> matricula.setAluno(aluno));

        try {
            return repository.save(aluno);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Já existe um aluno cadastrado com este CPF ou E-mail.");
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar aluno.", e);
        }
    }

    public Page<Aluno> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public List<Aluno> listarTodosInativos() {
        return repository.findByStatusIgnoreCase("INATIVO");
    }

    public Page<Aluno> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Page<Aluno> listarPendentes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("PENDENTE", pageable);
    }

    public Page<Aluno> filtrarPesquisa(String pesquisa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (pesquisa == null || pesquisa.isBlank()) {
            return Page.empty(pageable);
        }

        pesquisa = pesquisa.trim();
        Page<Aluno> resultados = repository.findByStatusIgnoreCaseAndNomeContainingIgnoreCase("ATIVO", pesquisa, pageable);

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEmailContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndCpfContainingIgnoreCase("ATIVO", pesquisa, pageable);
        }

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase("ATIVO", pesquisa, pageable);
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

        return resultados;
    }


    public Page<Aluno> filtrar(List<Curso> cursos, String genero, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        boolean temCursos = (cursos != null && !cursos.isEmpty());
        boolean temGenero = (genero != null && !genero.isBlank() && !genero.equalsIgnoreCase("TODOS"));

        if (temCursos && temGenero) {
            return repository.findByStatusIgnoreCaseAndMatriculas_CursoInAndGeneroIgnoreCase(
                    "ATIVO", cursos, genero, pageable
            );
        } else if (temCursos) {
            return repository.findByStatusIgnoreCaseAndMatriculas_CursoIn(
                    "ATIVO", cursos, pageable
            );
        } else if (temGenero) {
            return repository.findByStatusIgnoreCaseAndGeneroIgnoreCase(
                    "ATIVO", genero, pageable
            );
        }

        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }


    public Aluno buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

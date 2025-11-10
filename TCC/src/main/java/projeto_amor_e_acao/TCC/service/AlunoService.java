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

import java.time.LocalDate;
import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    @Transactional
    public Aluno salvar(Aluno aluno) {
        aluno.setDataAlteracaoStatus(LocalDate.now());
        aluno.getMatriculas().forEach(matricula -> matricula.setAluno(aluno));

        var existenteCpf = repository.findByCpfIgnoreCase(aluno.getCpf());
        if (existenteCpf.isPresent() && !existenteCpf.get().getId().equals(aluno.getId())) {
            throw new IllegalStateException("( Esse CPF já existe! )");
        }

        var existenteEmail = repository.findByEmailIgnoreCase(aluno.getEmail());
        if (existenteEmail.isPresent() && !existenteEmail.get().getId().equals(aluno.getId())) {
            throw new IllegalStateException("( Esse E-mail já existe! )");
        }

        try {
            return repository.save(aluno);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar aluno.", e);
        }
    }


    public Page<Aluno> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public Page<Aluno> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Page<Aluno> listarPendentes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("PENDENTE", pageable);
    }

    public Page<Aluno> filtrarPesquisa(String status, String pesquisa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (pesquisa == null || pesquisa.isBlank()) {
            return Page.empty(pageable);
        }

        pesquisa = pesquisa.trim();
        Page<Aluno> resultados = repository.findByStatusIgnoreCaseAndNomeContainingIgnoreCase(status, pesquisa, pageable);

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

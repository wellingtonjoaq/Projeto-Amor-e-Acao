package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;


public interface CursoRepository extends JpaRepository<Curso, Long> {

    Page<Curso> findByStatusIgnoreCase(String status, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndCategoriasContainingIgnoreCase(String status, String categoria, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndPeriodoIgnoreCase(String status, String periodo, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndCategoriasContainingIgnoreCaseAndPeriodoIgnoreCase(String status, String categoria, String periodo, Pageable pageable);

    Page<Curso> findByCategoriasContainingIgnoreCase(String categoria, Pageable pageable);

    Page<Curso> findByPeriodoIgnoreCase(String periodo, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndNomeContainingIgnoreCase(String status, String nome, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndProfessorContainingIgnoreCase(String status, String pesquisa, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndCepContainingIgnoreCase(String status, String pesquisa, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase(String status, String pesquisa, Pageable pageable);

    Page<Curso> findByStatusIgnoreCaseAndBairroContainingIgnoreCase(String status, String pesquisa, Pageable pageable);
}


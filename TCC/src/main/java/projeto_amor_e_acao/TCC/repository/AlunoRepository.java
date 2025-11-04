package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Long countByStatus(String status);

    @Query("SELECT COUNT(a) FROM Aluno a JOIN a.matriculas m " +
            "WHERE m.curso.id = :cursoId")
    Long countAlunosMatriculadosByCursoId(@Param("cursoId") Long cursoId);

    @Query("SELECT COUNT(a) FROM Aluno a JOIN a.matriculas m WHERE m.curso.id " +
            "= :cursoId AND a.status = :status")
    Long countAlunosEvasivosByCursoIdAndStatus(
            @Param("cursoId") Long cursoId, @Param("status") String status);

    @Query("SELECT a FROM Aluno a JOIN a.matriculas m WHERE m.curso.id " +
            "= :cursoId AND a.status = :status")
    List<Aluno> findAlunosEvasivosByCursoIdAndStatus(
            @Param("cursoId") Long cursoId, @Param("status") String status);

    List<Aluno> findByStatusIgnoreCase(String status);

    Page<Aluno> findByStatusIgnoreCase(
            String status, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndGeneroIgnoreCase(
            String status, String genero, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndMatriculas_CursoIn(
            String status, List<Curso> cursos, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndMatriculas_CursoInAndGeneroIgnoreCase(
            String status, List<Curso> cursos, String genero, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndNomeContainingIgnoreCase(
            String status, String nome, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndCpfContainingIgnoreCase(
            String status, String cpf, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndEmailContainingIgnoreCase(
            String status, String email, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase(
            String status, String telefone, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndCepContainingIgnoreCase(
            String status, String cep, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase(
            String status, String endereco, Pageable pageable);

    Page<Aluno> findByStatusIgnoreCaseAndBairroContainingIgnoreCase(
            String status, String bairro, Pageable pageable);
}

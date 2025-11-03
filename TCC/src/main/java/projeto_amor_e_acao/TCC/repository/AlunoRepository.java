package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto_amor_e_acao.TCC.model.Aluno;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    List<Aluno> findByStatusIgnoreCase(String status);

    Long countByStatus(String status);

    @Query("SELECT COUNT(a) FROM Aluno a JOIN a.matriculas m WHERE m.curso.id = :cursoId")
    Long countAlunosMatriculadosByCursoId(@Param("cursoId") Long cursoId);

    @Query("SELECT COUNT(a) FROM Aluno a JOIN a.matriculas m WHERE m.curso.id = " +
            ":cursoId AND a.status = :status")
    Long countAlunosEvasivosByCursoIdAndStatus(
            @Param("cursoId") Long cursoId, @Param("status") String status);

}

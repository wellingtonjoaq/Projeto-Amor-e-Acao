package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.Aluno;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Page<Aluno> findByStatusIgnoreCase(String status, Pageable pageable);
    List<Aluno> findByStatusIgnoreCase(String status);
}

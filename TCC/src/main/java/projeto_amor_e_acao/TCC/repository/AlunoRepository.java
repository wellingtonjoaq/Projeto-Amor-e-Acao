package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByStatusIgnoreCase(String status);
}

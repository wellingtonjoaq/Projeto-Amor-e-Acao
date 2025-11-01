package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.util.List;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    Page<Voluntario> findByStatusIgnoreCase(String status, Pageable pageable);
    List<Voluntario> findByStatusIgnoreCase(String status);
}

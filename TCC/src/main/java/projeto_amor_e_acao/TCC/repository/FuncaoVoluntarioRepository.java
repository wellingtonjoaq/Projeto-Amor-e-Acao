package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;

import java.util.List;

public interface FuncaoVoluntarioRepository extends JpaRepository<FuncaoVoluntario, Long> {

    List<FuncaoVoluntario> findByNomeContainingIgnoreCase(String nome);
}

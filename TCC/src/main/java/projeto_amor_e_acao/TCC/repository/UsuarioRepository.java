package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Page<Usuario> findByStatusIgnoreCase(String status, Pageable pageable);
    List<Usuario> findByStatusIgnoreCase(String status);
}

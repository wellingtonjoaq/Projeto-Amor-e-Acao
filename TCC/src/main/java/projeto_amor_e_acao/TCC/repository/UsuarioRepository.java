package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByStatus(Usuario.Status status);

}

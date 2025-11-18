package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.RedefinirSenhaToken;
import projeto_amor_e_acao.TCC.model.Usuario;

import java.util.Optional;

public interface RedefinirSenhaTokenRepository extends JpaRepository<RedefinirSenhaToken, Long> {

    RedefinirSenhaToken findByToken(String token);

    Optional<RedefinirSenhaToken> findByUsuario(Usuario usuario);

}

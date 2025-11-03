package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projeto_amor_e_acao.TCC.model.LogAuditoria;

import java.util.List;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {

    @Query("SELECT CONCAT(l.usuarioId, ' - ', l.acao) AS key, COUNT(l) FROM " +
            "LogAuditoria l GROUP BY l.usuarioId, l.acao")
    List<Object[]> countAcoesAgrupadasPorUsuarioEAcao();

    @Query("SELECT COUNT(l) FROM LogAuditoria l WHERE l.acao IN ('CREATE', 'UPDATE', 'DELETE')")
    Long countTotalAcoesChave();
}

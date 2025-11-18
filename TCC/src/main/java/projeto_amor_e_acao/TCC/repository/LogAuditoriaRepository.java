package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projeto_amor_e_acao.TCC.model.LogAuditoria;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {

    @Query("SELECT CONCAT(l.usuarioId, ' - ', l.acao) AS key, COUNT(l) FROM " +
            "LogAuditoria l GROUP BY l.usuarioId, l.acao")
    List<Object[]> countAcoesAgrupadasPorUsuarioEAcao();

    @Query("SELECT COUNT(l) FROM LogAuditoria l WHERE l.acao IN ('CREATE', 'UPDATE', 'DELETE')")
    Long countTotalAcoesChave();

    @Query("SELECT COUNT(l) FROM LogAuditoria l WHERE l.dataHora BETWEEN :dataInicio AND :dataFim")
    long countTotalAcoesChaveBetweenDates(LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("SELECT CONCAT(l.usuarioId, ' - ', l.acao) AS key, COUNT(l) FROM LogAuditoria l WHERE l.dataHora BETWEEN :dataInicio AND :dataFim GROUP BY l.usuarioId, l.acao")
    List<Object[]> countAcoesAgrupadasPorUsuarioEAcaoBetweenDates(LocalDateTime dataInicio, LocalDateTime dataFim);
}

package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.util.List;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    List<Voluntario> findByStatusIgnoreCase(String status);

    @Query("SELECT v.funcao.nome, COUNT(v) FROM Voluntario v WHERE v.status = " +
            "'ATIVO' GROUP BY v.funcao.nome")
    List<Object[]> countActiveVolunteersByFunction();

    @Query("SELECT v.id, v.dataAlteracaoStatus FROM Voluntario v WHERE v.status = " +
            "'INATIVO' AND v.dataAlteracaoStatus IS NOT NULL")
    List<Object[]> findInactiveVolunteersStatusChangeDate();

    @Query("SELECT v.id, v.dataAlteracaoStatus FROM Voluntario v WHERE v.status = 'ATIVO'")
    List<Object[]> findActiveVolunteersStatusChangeDate();

    @Query("SELECT v.genero, COUNT(v) FROM Voluntario v GROUP BY v.genero")
    List<Object[]> countByGender();

    @Query("SELECT v.motivacao, COUNT(v) FROM Voluntario v GROUP BY v.motivacao")
    List<Object[]> countByMotivation();

}

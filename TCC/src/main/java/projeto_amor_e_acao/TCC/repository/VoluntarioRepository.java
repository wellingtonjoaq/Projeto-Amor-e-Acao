package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    @Query("SELECT v.funcao.nome, COUNT(v) FROM Voluntario v WHERE v.status = " +
            "'ATIVO' GROUP BY v.funcao.nome")
    List<Object[]> countActiveVolunteersByFunction();

    @Query("SELECT v.genero, COUNT(v) FROM Voluntario v GROUP BY v.genero")
    List<Object[]> countByGender();

    @Query("SELECT v.motivacao, COUNT(v) FROM Voluntario v GROUP BY v.motivacao")
    List<Object[]> countByMotivation();

    @Query("SELECT v.funcao.nome, COUNT(v) FROM Voluntario v WHERE v.status = 'ATIVO' AND v.dataAlteracaoStatus BETWEEN :dataInicio AND :dataFim GROUP BY v.funcao.nome")
    List<Object[]> countActiveVolunteersByFunctionBetweenDates(LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT v.genero, COUNT(v) FROM Voluntario v WHERE v.dataAlteracaoStatus BETWEEN :dataInicio AND :dataFim GROUP BY v.genero")
    List<Object[]> countByGenderBetweenDates(LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT v.motivacao, COUNT(v) FROM Voluntario v WHERE v.dataAlteracaoStatus BETWEEN :dataInicio AND :dataFim GROUP BY v.motivacao")
    List<Object[]> countByMotivationBetweenDates(LocalDate dataInicio, LocalDate dataFim);

    List<Voluntario> findByStatusIgnoreCase(String status);

    Page<Voluntario> findByStatusIgnoreCase(String status, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndGeneroIgnoreCase(String status, String genero, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndFuncaoIn(String status, List<FuncaoVoluntario> funcao, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndFuncaoInAndGeneroIgnoreCase(String status, List<FuncaoVoluntario> funcao, String genero, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndNomeContainingIgnoreCase(String status, String nome, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndCpfContainingIgnoreCase(String status, String cpf, Pageable pageable);

    Optional<Voluntario> findByCpfIgnoreCase(String cpf);

    Page<Voluntario> findByStatusIgnoreCaseAndEmailContainingIgnoreCase(String status, String email, Pageable pageable);

    Optional<Voluntario> findByEmailIgnoreCase(String email);

    Page<Voluntario> findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase(String status, String telefone, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndCepContainingIgnoreCase(String status, String cep, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase(String status, String endereco, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndBairroContainingIgnoreCase(String status, String bairro, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndCidadeContainingIgnoreCase(String status, String cidade, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndEstadoContainingIgnoreCase(String status, String estado, Pageable pageable);

}

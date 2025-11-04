package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.util.List;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    List<Voluntario> findByStatusIgnoreCase(String status);

    Page<Voluntario> findByStatusIgnoreCase(String status, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndGeneroIgnoreCase(String status, String genero, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndFuncaoIn(String status, List<FuncaoVoluntario> funcao, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndFuncaoInAndGeneroIgnoreCase(String status, List<FuncaoVoluntario> funcao, String genero, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndNomeContainingIgnoreCase(String status, String nome, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndCpfContainingIgnoreCase(String status, String cpf, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndEmailContainingIgnoreCase(String status, String email, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase(String status, String telefone, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndCepContainingIgnoreCase(String status, String cep, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase(String status, String endereco, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndBairroContainingIgnoreCase(String status, String bairro, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndCidadeContainingIgnoreCase(String status, String cidade, Pageable pageable);

    Page<Voluntario> findByStatusIgnoreCaseAndEstadoContainingIgnoreCase(String status, String estado, Pageable pageable);
}

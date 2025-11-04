package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {
    Optional<EmpresaParceira> findByCnpj(String cnpj);

    List<EmpresaParceira> findByStatusIgnoreCase(String status);

    Page<EmpresaParceira> findByStatusIgnoreCase(String status, Pageable pageable);

    Page<EmpresaParceira> findByStatusIgnoreCaseAndNomeContainingIgnoreCase(String status, String nome, Pageable pageable);

    Page<EmpresaParceira> findByStatusIgnoreCaseAndCnpjContainingIgnoreCase(String status, String cnpj, Pageable pageable);

    Page<EmpresaParceira> findByStatusIgnoreCaseAndEnderecoContainingIgnoreCase(String status, String endereco, Pageable pageable);

    Page<EmpresaParceira> findByStatusIgnoreCaseAndNomeRepresentanteContainingIgnoreCase(String status, String nome, Pageable pageable);

    Page<EmpresaParceira> findByStatusIgnoreCaseAndCpfRepresentanteContainingIgnoreCase(String status, String cpf, Pageable pageable);

    Page<EmpresaParceira> findByStatusIgnoreCaseAndEmailContainingIgnoreCase(String status, String email, Pageable pageable);

    Page<EmpresaParceira> findByStatusIgnoreCaseAndTelefoneContainingIgnoreCase(String status, String telefone, Pageable pageable);
}

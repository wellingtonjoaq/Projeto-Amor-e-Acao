package projeto_amor_e_acao.TCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {
    Optional<EmpresaParceira> findByCnpj(String cnpj);

    List<EmpresaParceira> findByStatus(EmpresaParceira.Status status);
}

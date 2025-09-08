package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.repository.EmpresaParceiraRepository;
import projeto_amor_e_acao.TCC.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaParceiraService {

    @Autowired
    private EmpresaParceiraRepository repository;

    public List<EmpresaParceira> findAll() {
        return repository.findAll();
    }

    public Optional<EmpresaParceira> findById(Long id) {
        return repository.findById(id);
    }

    public EmpresaParceira save(EmpresaParceira empresaParceira) {
        return repository.save(empresaParceira);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

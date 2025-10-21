package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.repository.VoluntarioRepository;

import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

    @Transactional
    public void salvar(Voluntario voluntario) {
        if (voluntario.getFuncao() != null && voluntario.getFuncao().getId() != null &&
                voluntario.getFuncao().getId() == 0) {
            voluntario.setFuncao(null);
        }
        repository.save(voluntario);
    }

    public List<Voluntario> listarAtivos() {return repository.findByStatusIgnoreCase("ATIVO");}

    public List<Voluntario> listarInativos() {
        return repository.findByStatusIgnoreCase("INATIVO");
    }

    public List<Voluntario> listarPendentes() {
        return repository.findByStatusIgnoreCase("PENDENTE");
    }


    public Voluntario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

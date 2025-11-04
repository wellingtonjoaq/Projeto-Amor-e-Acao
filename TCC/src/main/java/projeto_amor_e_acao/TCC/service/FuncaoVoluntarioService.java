package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.repository.FuncaoVoluntarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FuncaoVoluntarioService {

    @Autowired
    private FuncaoVoluntarioRepository repository;

    @Transactional
    public void salvar(FuncaoVoluntario funcaoVoluntario) {
        try {
            repository.save(funcaoVoluntario);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar função de voluntario.", e);
        }
    }

    public List<FuncaoVoluntario> listarTodos() {
        return repository.findAll();
    }

    public List<FuncaoVoluntario> buscarPorIds(List<Long> ids){
        return repository.findAllById(ids);
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

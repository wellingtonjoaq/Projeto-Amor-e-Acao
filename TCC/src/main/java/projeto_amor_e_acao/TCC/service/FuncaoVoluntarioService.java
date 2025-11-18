package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.repository.FuncaoVoluntarioRepository;

import java.util.ArrayList;
import java.util.List;

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

    public List<FuncaoVoluntario> filtrarPesquisa(String pesquisa) {
        if (pesquisa == null || pesquisa.isBlank()) {
            return new ArrayList<>();
        }

        pesquisa = pesquisa.trim();
        List<FuncaoVoluntario> resultados = repository.findByNomeContainingIgnoreCase(pesquisa);

        if (resultados == null || resultados.isEmpty()) {
            return new ArrayList<>();
        }

        return resultados;
    }



    public FuncaoVoluntario buscarPorId(Long id){
        return repository.findById(id).orElseThrow();
    }

    public List<FuncaoVoluntario> buscarPorIds(List<Long> ids){
        return repository.findAllById(ids);
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

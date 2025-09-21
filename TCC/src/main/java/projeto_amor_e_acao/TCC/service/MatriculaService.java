package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.Matricula;
import projeto_amor_e_acao.TCC.repository.MatriculaRepository;

import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository repository;

    @Transactional
    public void salvar(Matricula matricula) {
        repository.save(matricula);
    }

    public List<Matricula> listarTodos() {
        var result = repository.findAll();
        return result;
    }

    public Matricula buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

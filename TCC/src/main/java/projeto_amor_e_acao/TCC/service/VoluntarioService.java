package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.repository.AlunoRepository;
import projeto_amor_e_acao.TCC.repository.VoluntarioRepository;

import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

    @Transactional
    public void salvar(Voluntario voluntario) {
        try {
            repository.save(voluntario);
        } catch (
        DataIntegrityViolationException e) {
            throw new IllegalStateException("Já existe um voluntario cadastrado com este CPF ou E-mail.");
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar voluntario.", e);
        }
    }

    public List<Voluntario> listarTodos() {
        var result = repository.findAll();
        return result;
    }

    public Voluntario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

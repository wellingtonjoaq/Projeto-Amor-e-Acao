package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.repository.AlunoRepository;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    @Transactional
    public Aluno salvar(Aluno aluno) {
            if (aluno.getNome().isBlank() || aluno.getCpf().isBlank() || aluno.getStatus().isBlank()){
                return aluno;
            }
            aluno.getMatriculas().forEach(matricula -> matricula.setAluno(aluno));

        return repository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        var result = repository.findAll();
        return result;
    }

    public Aluno buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

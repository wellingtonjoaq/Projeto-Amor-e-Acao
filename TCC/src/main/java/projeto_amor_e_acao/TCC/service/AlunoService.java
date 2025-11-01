package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        aluno.getMatriculas().forEach(matricula -> matricula.setAluno(aluno));

        try {
            return repository.save(aluno);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Já existe um aluno cadastrado com este CPF ou E-mail.");
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar aluno.", e);
        }
    }

    public Page<Aluno> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public List<Aluno> listarTodosInativos() {
        return repository.findByStatusIgnoreCase("INATIVO");
    }

    public Page<Aluno> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Page<Aluno> listarPendentes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("PENDENTE", pageable);
    }


    public Aluno buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}

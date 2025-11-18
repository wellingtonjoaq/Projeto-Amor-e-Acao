package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.dto.RelatorioEvasaoDTO;
import projeto_amor_e_acao.TCC.repository.AlunoRepository;
import projeto_amor_e_acao.TCC.repository.CursoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioEvasaoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<RelatorioEvasaoDTO> calcularRelatorioEvasao() {
        List<RelatorioEvasaoDTO> resultados = new ArrayList<>();
        List<Curso> cursos = cursoRepository.findAll();

        for (Curso curso : cursos) {
            Long totalMatriculados = alunoRepository.countAlunosMatriculadosByCursoId(curso.getId());

            Long totalEvasivos = alunoRepository.countAlunosEvasivosByCursoIdAndStatus(
                    curso.getId(), "INATIVO");

            double taxaEvasao = 0.0;
            if (totalMatriculados != null && totalMatriculados > 0) {
                taxaEvasao = (double) totalEvasivos / totalMatriculados * 100.0;
            }

            RelatorioEvasaoDTO dto = new RelatorioEvasaoDTO();
            dto.setNomeCurso(curso.getNome());
            dto.setTotalMatriculados(totalMatriculados);
            dto.setTotalEvasivos(totalEvasivos);
            dto.setTaxaEvasao(taxaEvasao);

            resultados.add(dto);
        }

        return resultados;
    }
}
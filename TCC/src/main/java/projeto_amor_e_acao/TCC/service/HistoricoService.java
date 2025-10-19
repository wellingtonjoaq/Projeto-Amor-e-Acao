package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.repository.AlunoRepository;
import projeto_amor_e_acao.TCC.repository.CursoRepository;
import projeto_amor_e_acao.TCC.repository.EmpresaParceiraRepository;

import java.util.*;

@Service
public class HistoricoService {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @Autowired
    private UsuarioService usuarioService;

    public List<Object> listarHistorico() {
        List<Object> lista = new ArrayList<>();

        lista.addAll(alunoService.listarInativos());
        lista.addAll(voluntarioService.listarInativos());
        lista.addAll(empresaParceiraService.listarInativos());
        lista.addAll(usuarioService.listarInativos());

        lista.sort((o1, o2) -> {
            try {
                var data1 = (Date) o1.getClass().getMethod("getDataAlteracaoStatus").invoke(o1);
                var data2 = (Date) o2.getClass().getMethod("getDataAlteracaoStatus").invoke(o2);

                if (data1 == null && data2 == null) return 0;
                if (data1 == null) return 1;
                if (data2 == null) return -1;

                return data2.compareTo(data1); // Mais recente primeiro
            } catch (Exception e) {
                throw new RuntimeException("Erro ao comparar datas de alteração", e);
            }
        });

        return lista;
    }
}

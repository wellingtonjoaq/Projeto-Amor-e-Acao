package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacaoService {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    public List<NotificacaoDTO> listarNotificacaoLimitado() {
        List<NotificacaoDTO> lista = new ArrayList<>();

        alunoService.listarPendentes(0, 20).forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));
        voluntarioService.listarPendentes(0,20).forEach(voluntario -> lista.add(mapVoluntarioToDTO(voluntario)));
        empresaParceiraService.listarPendentes(0,20).forEach(empresa -> lista.add(mapEmpresaToDTO(empresa)));

        lista.sort(Comparator.comparing(NotificacaoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return lista.stream().limit(10).collect(Collectors.toList());
    }

    public List<NotificacaoDTO> listarNotificacao(int page, int size) {
        List<NotificacaoDTO> lista = new ArrayList<>();

        alunoService.listarPendentes(page, size).forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));
        voluntarioService.listarPendentes(page, size).forEach(voluntario -> lista.add(mapVoluntarioToDTO(voluntario)));
        empresaParceiraService.listarPendentes(page, size).forEach(empresa -> lista.add(mapEmpresaToDTO(empresa)));

        lista.sort(Comparator.comparing(NotificacaoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return lista;
    }

    private NotificacaoDTO mapAlunoToDTO(Aluno a) {
        return new NotificacaoDTO(
                a.getId(),
                a.getNome(),
                a.getDataAlteracaoStatus(),
                "Aluno"
        );
    }

    private NotificacaoDTO mapVoluntarioToDTO(Voluntario v) {
        return new NotificacaoDTO(
                v.getId(),
                v.getNome(),
                v.getDataAlteracaoStatus(),
                "Voluntario"
        );
    }

    private NotificacaoDTO mapEmpresaToDTO(EmpresaParceira e) {
        return new NotificacaoDTO(
                e.getId(),
                e.getNome(),
                e.getDataAlteracaoStatus(),
                "EmpresaParceira"
        );
    }
}

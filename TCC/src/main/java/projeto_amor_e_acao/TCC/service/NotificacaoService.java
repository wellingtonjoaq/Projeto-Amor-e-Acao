package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Voluntario;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificacaoService {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    public List<NotificacaoDTO> listarNotificacaoLimitado(int limiteTotal) {
        List<NotificacaoDTO> lista = new ArrayList<>();

        alunoService.listarPendentes(0, limiteTotal).forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));
        voluntarioService.listarPendentes(0, limiteTotal).forEach(voluntario -> lista.add(mapVoluntarioToDTO(voluntario)));
        empresaParceiraService.listarPendentes(0, limiteTotal).forEach(empresa -> lista.add(mapEmpresaToDTO(empresa)));

        lista.sort(Comparator.comparing(NotificacaoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return lista.stream().limit(limiteTotal).collect(Collectors.toList());
    }

    public Page<NotificacaoDTO> listarNotificacao(int page, int size) {
        List<NotificacaoDTO> lista = new ArrayList<>();

        alunoService.listarPendentes(0, Integer.MAX_VALUE)
                .forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));

        voluntarioService.listarPendentes(0, Integer.MAX_VALUE)
                .forEach(voluntario -> lista.add(mapVoluntarioToDTO(voluntario)));

        empresaParceiraService.listarPendentes(0, Integer.MAX_VALUE)
                .forEach(empresa -> lista.add(mapEmpresaToDTO(empresa)));

        lista.sort(Comparator.comparing(
                NotificacaoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<NotificacaoDTO> pagina = (start > lista.size()) ? Collections.emptyList() : lista.subList(start, end);

        return new PageImpl<>(pagina, pageable, lista.size());
    }

    public Page<NotificacaoDTO> filtroNotificacao(
            LocalDate dataInicio,
            LocalDate dataFim,
            List<String> tipos,
            int page,
            int size)
    {
        List<NotificacaoDTO> lista = new ArrayList<>();

        if (tipos == null || tipos.isEmpty()) {
            tipos = Arrays.asList("Aluno", "Voluntario", "Empresa Parceira");
        }

        if (tipos.contains("Aluno")) {
            alunoService.listarPendentes(0, Integer.MAX_VALUE)
                    .getContent()
                    .forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));
        }

        if (tipos.contains("Voluntario")) {
            voluntarioService.listarPendentes(0, Integer.MAX_VALUE)
                    .getContent()
                    .forEach(v -> lista.add(mapVoluntarioToDTO(v)));
        }

        if (tipos.contains("Empresa Parceira")) {
            empresaParceiraService.listarPendentes(0, Integer.MAX_VALUE)
                    .getContent()
                    .forEach(e -> lista.add(mapEmpresaToDTO(e)));
        }

        List<NotificacaoDTO> filtrada = lista.stream()
                .filter(n -> n.getDataAlteracaoStatus() != null)
                .filter(n -> {
                    LocalDate data = n.getDataAlteracaoStatus();

                    if (dataInicio != null && dataFim == null) {
                        return !data.isBefore(dataInicio);
                    }
                    if (dataFim != null && dataInicio == null) {
                        return !data.isAfter(dataFim);
                    }
                    if (dataInicio != null && dataFim != null) {
                        return !data.isBefore(dataInicio) && !data.isAfter(dataFim);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        filtrada.sort(Comparator.comparing(
                NotificacaoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtrada.size());

        List<NotificacaoDTO> pagina =
                (start >= filtrada.size()) ? Collections.emptyList() : filtrada.subList(start, end);

        return new PageImpl<>(pagina, pageable, filtrada.size());
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
                "Empresa Parceira"
        );
    }
}

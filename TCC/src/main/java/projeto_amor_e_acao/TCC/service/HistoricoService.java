package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.dto.HistoricoDTO;
import projeto_amor_e_acao.TCC.model.*;

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

    public Page<HistoricoDTO> listarHistorico(int page, int size) {
        List<HistoricoDTO> lista = new ArrayList<>();

        alunoService.listarInativos(0, Integer.MAX_VALUE)
                .forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));

        voluntarioService.listarInativos(0, Integer.MAX_VALUE)
                .forEach(voluntario -> lista.add(mapVoluntarioToDTO(voluntario)));

        empresaParceiraService.listarInativos(0, Integer.MAX_VALUE)
                .forEach(empresa -> lista.add(mapEmpresaToDTO(empresa)));

        usuarioService.listarInativos(0, Integer.MAX_VALUE)
                .forEach(usuario -> lista.add(mapUsuarioToDTO(usuario)));

        lista.sort(Comparator.comparing(
                HistoricoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<HistoricoDTO> pagina = (start > lista.size()) ? Collections.emptyList() : lista.subList(start, end);

        return new PageImpl<>(pagina, pageable, lista.size());
    }

    public Page<HistoricoDTO> filtrarPesquisaHistorico(String pesquisa, int page, int size) {
        List<HistoricoDTO> lista = new ArrayList<>();

        alunoService.filtrarPesquisa("INATIVO", pesquisa,0, Integer.MAX_VALUE)
                .forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));

        voluntarioService.filtrarPesquisa("INATIVO", pesquisa,0, Integer.MAX_VALUE)
                .forEach(voluntario -> lista.add(mapVoluntarioToDTO(voluntario)));

        empresaParceiraService.filtrarPesquisa("INATIVO", pesquisa, 0, Integer.MAX_VALUE)
                .forEach(empresa -> lista.add(mapEmpresaToDTO(empresa)));

        usuarioService.filtrarPesquisa("INATIVO", pesquisa,0, Integer.MAX_VALUE)
                .forEach(usuario -> lista.add(mapUsuarioToDTO(usuario)));

        lista.sort(Comparator.comparing(
                HistoricoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<HistoricoDTO> pagina = (start > lista.size()) ? Collections.emptyList() : lista.subList(start, end);

        return new PageImpl<>(pagina, pageable, lista.size());
    }


    private HistoricoDTO mapAlunoToDTO(Aluno a) {
        return new HistoricoDTO(
                a.getId(),
                a.getNome(),
                a.getEmail(),
                a.getStatus(),
                a.getDataAlteracaoStatus(),
                "Aluno"
        );
    }

    private HistoricoDTO mapVoluntarioToDTO(Voluntario v) {
        return new HistoricoDTO(
                v.getId(),
                v.getNome(),
                v.getEmail(),
                v.getStatus(),
                v.getDataAlteracaoStatus(),
                "Voluntario"
        );
    }

    private HistoricoDTO mapEmpresaToDTO(EmpresaParceira e) {
        return new HistoricoDTO(
                e.getId(),
                e.getNome(),
                e.getEmail(),
                e.getStatus(),
                e.getDataAlteracaoStatus(),
                "EmpresaParceira"
        );
    }

    private HistoricoDTO mapUsuarioToDTO(Usuario u) {
        return new HistoricoDTO(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getStatus(),
                u.getDataAlteracaoStatus(),
                "Usuario"
        );
    }
}

package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<HistoricoDTO> listarHistorico() {
        List<HistoricoDTO> lista = new ArrayList<>();

        alunoService.listarInativos().forEach(aluno -> lista.add(mapAlunoToDTO(aluno)));
        voluntarioService.listarInativos().forEach(voluntario -> lista.add(mapVoluntarioToDTO(voluntario)));
        empresaParceiraService.listarInativos().forEach(empresa -> lista.add(mapEmpresaToDTO(empresa)));
        usuarioService.listarInativos().forEach(usuario -> lista.add(mapUsuarioToDTO(usuario)));

        // Ordena por data mais recente
        lista.sort(Comparator.comparing(HistoricoDTO::getDataAlteracaoStatus,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return lista;
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

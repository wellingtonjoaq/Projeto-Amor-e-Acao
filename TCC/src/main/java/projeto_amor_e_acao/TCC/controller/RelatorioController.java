package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.dto.RelatorioEngajamentoDTO;
import projeto_amor_e_acao.TCC.dto.RelatorioEvasaoDTO;
import projeto_amor_e_acao.TCC.dto.RelatorioVoluntarioDTO;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.repository.CursoRepository;
import projeto_amor_e_acao.TCC.service.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("relatorio")
public class RelatorioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private RelatorioEvasaoService relatorioEvasaoService;

    @Autowired
    private RelatorioVoluntarioService relatorioVoluntarioService;

    @Autowired
    private RelatorioEngajamentoService relatorioEngajamentoService;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/listar")
    public String listarRelatorio(
            @RequestParam(name = "tipoRelatorio", defaultValue = "alunos") String tipoRelatorio,
            @RequestParam(name = "curso", required = false) Long cursoIdFiltro,
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioLogado();
        model.addAttribute("usuarioLogado", usuario);

        model.addAttribute("tipoRelatorio", tipoRelatorio);
        model.addAttribute("cursoFiltro", cursoIdFiltro);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);

        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);

        LocalDateTime dataInicioDateTime = dataInicio != null ? dataInicio.atStartOfDay() : null;
        LocalDateTime dataFimDateTime = dataFim != null ? dataFim.atTime(23, 59, 59) : null;

        switch (tipoRelatorio) {
            case "alunos":
                List<RelatorioEvasaoDTO> relatorioAlunos;
                if (cursoIdFiltro != null) {
                    relatorioAlunos = relatorioEvasaoService.calcularRelatorioEvasaoPorCursoId(cursoIdFiltro);
                } else {
                    relatorioAlunos = relatorioEvasaoService.calcularRelatorioEvasao();
                }
                model.addAttribute("relatorioAlunos", relatorioAlunos);
                break;

            case "voluntarios":
                RelatorioVoluntarioDTO relatorioVoluntarios = relatorioVoluntarioService.gerarRelatorioVoluntariadoFiltrado(dataInicioDateTime, dataFimDateTime);
                model.addAttribute("relatorioVoluntarios", Collections.singletonList(relatorioVoluntarios));
                break;

            case "engajamento":
                List<RelatorioEngajamentoDTO> relatorioEngajamento = Collections.singletonList(
                        relatorioEngajamentoService.gerarRelatorioEngajamentoFiltrado(dataInicioDateTime, dataFimDateTime));
                model.addAttribute("relatorioEngajamento", relatorioEngajamento);
                break;

            default:
                model.addAttribute("relatorioAlunos", Collections.emptyList());
                break;
        }

        return "administrativo/relatorio/listar";
    }

    @ModelAttribute("notificacoesMenu")
    public List<NotificacaoDTO> carregarNotifMenu() {
        return notificacaoService.listarNotificacaoLimitado(7);
    }

    @GetMapping("/listarUsuarioSimples")
    public String mostrarPaginaListarUsuarioSimples(Model model) {
        return "usuario-simples/relatorio/listar";
    }

    @GetMapping("/relatorios/alunos/pdf")
    public ResponseEntity<InputStreamResource> exportarRelatorioAlunos()
            throws IOException
    {
        return baixarArquivo("caminho/para/relatorio_alunos.pdf",
                "relatorio_alunos.pdf");
    }

    @GetMapping("/relatorios/voluntariado/pdf")
    public ResponseEntity<InputStreamResource> exportarRelatorioVoluntariado()
            throws IOException
    {
        return baixarArquivo("caminho/para/relatorio_voluntariado.pdf",
                "relatorio_voluntariado.pdf");
    }

    @GetMapping("/relatorios/engajamento/pdf")
    public ResponseEntity<InputStreamResource> exportarRelatorioEngajamento()
            throws IOException
    {
        return baixarArquivo("caminho/para/relatorio_engajamento.pdf",
                "relatorio_engajamento.pdf");
    }

    private ResponseEntity<InputStreamResource> baixarArquivo(
            String caminhoArquivo, String nomeDownload) throws IOException
    {
        File arquivo = new File(caminhoArquivo);

        if(!arquivo.exists())
        {
            throw new FileNotFoundException(
                    "Arquivo não encontrado: " + caminhoArquivo);
        }

        InputStreamResource recurso =
                new InputStreamResource(new FileInputStream(arquivo));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + nomeDownload)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(arquivo.length()).body(recurso);
    }
}
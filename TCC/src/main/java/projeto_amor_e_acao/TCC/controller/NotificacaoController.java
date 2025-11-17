package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.dto.HistoricoDTO;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("notificacao")
public class NotificacaoController {

    @Autowired
    private NotificacaoService service;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado() {
        return usuarioService.getUsuarioLogado();
    }

    @ModelAttribute("notificacoesMenu")
    public List<NotificacaoDTO> carregarNotifMenu() {
        return service.listarNotificacaoLimitado(7);
    }

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size,
                         Model model) {
        model.addAttribute("notificacoes", service.listarNotificacao(page, size));
        model.addAttribute("paginaAtual", page);
        return "administrativo/notificacao/lista";
    }

    @GetMapping("filtrar")
    public String filtrar(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) List<String> tipos,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        if (tipos == null) {
            tipos = new ArrayList<>();
        }

        boolean semFiltros =
                tipos.isEmpty() &&
                        (dataInicio == null || dataInicio.isBlank()) &&
                        (dataFim == null || dataFim.isBlank());

        if (semFiltros) {
            return "redirect:/notificacao/listar";
        }

        LocalDate inicio = (dataInicio == null || dataInicio.isBlank()) ? null : LocalDate.parse(dataInicio);
        LocalDate fim = (dataFim == null || dataFim.isBlank()) ? null : LocalDate.parse(dataFim);

        Page<NotificacaoDTO> notificacoes =
                service.filtroNotificacao(inicio, fim, tipos, page, size);

        model.addAttribute("notificacoes", notificacoes);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("listaTipo", tipos);
        model.addAttribute("dataInicio", inicio);
        model.addAttribute("dataFim", fim);
        model.addAttribute("vazio", notificacoes.isEmpty());

        return "administrativo/notificacao/filtro/lista";
    }


    @GetMapping("/visualiza{tipo}/{id}")
    public String visualizar(@PathVariable String tipo, @PathVariable Long id, Model model) {

        switch (tipo) {
            case "Aluno":
                model.addAttribute("aluno", alunoService.buscarPorId(id));
                return "administrativo/notificacao/visualizaAluno";

            case "Voluntario":
                model.addAttribute("voluntario", voluntarioService.buscarPorId(id));
                return "administrativo/notificacao/visualizaVoluntario";

            case "EmpresaParceira":
                model.addAttribute("empresa", empresaParceiraService.buscarPorId(id));
                return "administrativo/notificacao/visualizaEmpresa";

            default:
                return "redirect:/notificacao/listar";
        }
    }


    @GetMapping("/ativarAluno/{id}")
    public String ativarAluno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Aluno aluno = alunoService.buscarPorId(id);

        aluno.setStatus("ATIVO");
        alunoService.salvar(aluno);
        redirectAttributes.addFlashAttribute("sucesso", "Aluno ativado com sucesso!");
        return "redirect:/notificacao/listar";
    }

    @GetMapping("ativarVoluntario/{id}")
    public String ativarVoluntario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Voluntario voluntario = voluntarioService.buscarPorId(id);

        voluntario.setStatus("ATIVO");
        voluntarioService.salvar(voluntario);
        redirectAttributes.addFlashAttribute("sucesso", "Voluntario ativado com sucesso!");
        return "redirect:/notificacao/listar";
    }

    @GetMapping("/ativarEmpresaParceira/{id}")
    public String ativarEmpresaParceira(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.buscarPorId(id);

        if (empresaParceira.isPresent()) {
            EmpresaParceira ep = empresaParceira.get();
            ep.setStatus("ATIVO");
            empresaParceiraService.salvar(ep);
        }
        redirectAttributes.addFlashAttribute("sucesso", "Empresa Parceira ativada com sucesso!");
        return "redirect:/notificacao/listar";
    }

    @PostMapping("/recusarAluno/{id}")
    public String recusarAluno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        alunoService.deletarPorId(id);
        redirectAttributes.addFlashAttribute("sucesso", "Aluno recusado com sucesso!");
        return "redirect:/notificacao/listar";
    }

    @PostMapping("recusarVoluntario/{id}")
    public String recusarVoluntario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        voluntarioService.deletarPorId(id);
        redirectAttributes.addFlashAttribute("sucesso", "Voluntario recusado com sucesso!");
        return "redirect:/notificacao/listar";
    }

    @PostMapping("/recusarEmpresaParceira/{id}")
    public String recusarEmpresaParceira(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        empresaParceiraService.deletarPorId(id);
        redirectAttributes.addFlashAttribute("sucesso", "Empresa Parceira recusada com sucesso!");
        return "redirect:/notificacao/listar";
    }
}

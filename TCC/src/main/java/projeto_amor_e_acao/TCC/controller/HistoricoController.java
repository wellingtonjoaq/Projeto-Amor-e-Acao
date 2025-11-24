package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.dto.HistoricoDTO;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.model.*;
import projeto_amor_e_acao.TCC.service.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("historico")
public class HistoricoController {

    @Autowired
    private HistoricoService service;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificacaoService notificacaoService;

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado() {
        return usuarioService.getUsuarioLogado();
    }

    @ModelAttribute("notificacoesMenu")
    public List<NotificacaoDTO> carregarNotifMenu() {
        return notificacaoService.listarNotificacaoLimitado(7);
    }

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        Page<HistoricoDTO> historicos = service.listarHistorico(page, size);
        model.addAttribute("historicos", historicos);
        return "administrativo/historico/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (pesquisa != null && !pesquisa.isEmpty()){
            Page<HistoricoDTO> historicos = service.filtrarPesquisaHistorico(pesquisa,page, size);
            model.addAttribute("historicos", historicos);
            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("vazio", historicos.isEmpty());

            return "administrativo/historico/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/historico/listar";
        }
    }

    @GetMapping("filtrar")
    public String filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false, name = "tipos") List<String> listaTipo,
            Model model) {

        Page<HistoricoDTO> historicos = service.filtroHistorico(listaTipo, page, size);

        model.addAttribute("historicos", historicos);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("listaTipo", listaTipo);
        model.addAttribute("vazio", false);


        if (listaTipo == null) {
            return "redirect:/historico/listar";
        }

        if (historicos.isEmpty()){
            model.addAttribute("vazio", historicos.isEmpty());
        }

        return "administrativo/historico/filtro/lista";
    }

    @GetMapping("visualizaAluno/{id}")
    public String visualizarAluno(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", alunoService.buscarPorId(id));
        return "administrativo/historico/visualizarAluno";
    }

    @GetMapping("visualizaVoluntario/{id}")
    public String visualizarVoluntario(@PathVariable Long id, Model model) {
        model.addAttribute("voluntario", voluntarioService.buscarPorId(id));
        return "administrativo/historico/visualizarVoluntario";
    }

    @GetMapping("visualizaEmpresaParceira/{id}")
    public String visualizarEmpresaParceira(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.buscarPorId(id);

            model.addAttribute("empresa", empresaParceira.get());
            return "administrativo/historico/visualizarEmpresaParceira";
    }

    @GetMapping("visualizaUsuario/{id}")
    public String visualizarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);

            model.addAttribute("usuario", usuarioOptional.get());
            return "administrativo/usuario/visualizar";
    }

    @PostMapping("/ativarAluno/{id}")
    public String ativarAluno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Aluno aluno = alunoService.buscarPorId(id);

        aluno.setStatus("ATIVO");
        alunoService.salvar(aluno);
        redirectAttributes.addFlashAttribute("sucesso", "Aluno ativado com sucesso!");
        return "redirect:/historico/listar";
    }

    @PostMapping("ativarVoluntario/{id}")
    public String ativarVoluntario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Voluntario voluntario = voluntarioService.buscarPorId(id);

        voluntario.setStatus("ATIVO");
        voluntarioService.salvar(voluntario);
        redirectAttributes.addFlashAttribute("sucesso", "Voluntario ativado com sucesso!");
        return "redirect:/historico/listar";
    }

    @PostMapping("/ativarEmpresaParceira/{id}")
    public String ativarEmpresaParceira(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.buscarPorId(id);

        if (empresaParceira.isPresent()) {
            EmpresaParceira ep = empresaParceira.get();
            ep.setStatus("ATIVO");
            empresaParceiraService.salvar(ep);
        }
        redirectAttributes.addFlashAttribute("sucesso", "Empresa Parceira ativada com sucesso!");
        return "redirect:/historico/listar";
    }


    @PostMapping("ativarUsuario/{id}")
    public String ativarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);

        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            user.setStatus("ATIVO");
            usuarioService.salvar(user);
        }
        redirectAttributes.addFlashAttribute("sucesso", "Usuario ativado com sucesso!");
        return "redirect:/historico/listar";
    }
}

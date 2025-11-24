package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.model.*;
import projeto_amor_e_acao.TCC.service.FuncaoVoluntarioService;
import projeto_amor_e_acao.TCC.service.NotificacaoService;
import projeto_amor_e_acao.TCC.service.UsuarioService;
import projeto_amor_e_acao.TCC.service.VoluntarioService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("voluntario")
public class VoluntarioController {

    @Autowired
    private VoluntarioService service;

    @Autowired
    private FuncaoVoluntarioService funcaoVoluntarioService;

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

    @GetMapping()
    public String formulario(Voluntario voluntario, Model model) {
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        return "administrativo/voluntario/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Voluntario voluntario, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (voluntario.getFuncao() != null && voluntario.getFuncao().getId() != null &&
                voluntario.getFuncao().getId() == 0) {
            voluntario.setFuncao(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "administrativo/voluntario/formulario";
        }

        try {
            service.salvar(voluntario);
            if (voluntario.getId() != null){
                redirectAttributes.addFlashAttribute("sucesso", "Voluntario atualizado com sucesso!");
            }
            else {
                redirectAttributes.addFlashAttribute("sucesso", "Voluntario salvo com sucesso!");
            }
            return "redirect:/voluntario/listar";
        }
        catch (IllegalStateException e) {
            if (e.getMessage().contains("CPF")) {
                result.rejectValue("cpf", "error.voluntario", e.getMessage());
            } else if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.voluntario", e.getMessage());
            }

            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "administrativo/voluntario/formulario";
        } catch (Exception e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "administrativo/voluntario/formulario";
        }
    }

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        Page<Voluntario> voluntarios = service.listarAtivos(page, size);

        model.addAttribute("voluntarios", voluntarios);
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        model.addAttribute("paginaAtual", page);
        return "administrativo/voluntario/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<Voluntario> voluntarios = service.filtrarPesquisa("ATIVO", pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("voluntarios", voluntarios);
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", voluntarios.isEmpty());

            return "administrativo/voluntario/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/voluntario/listar";
        }
    }

    @GetMapping("filtrar")
    public String filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false, name = "funcoes") List<Long> funcoesIds,
            @RequestParam(required = false) Boolean nome,
            @RequestParam(required = false) Boolean cpf,
            @RequestParam(required = false) Boolean email,
            @RequestParam(required = false) Boolean telefone,
            @RequestParam(required = false) Boolean cep,
            @RequestParam(required = false) Boolean bairro,
            @RequestParam(required = false) Boolean endereco,
            @RequestParam(required = false) Boolean cidade,
            @RequestParam(required = false) Boolean estado,
            Model model) {

        List<FuncaoVoluntario> funcoes = (funcoesIds != null && !funcoesIds.isEmpty())
                ? funcaoVoluntarioService.buscarPorIds(funcoesIds)
                : Collections.emptyList();

        boolean temFuncao = !funcoes.isEmpty();
        boolean temGenero = (genero != null && !genero.isBlank());

        Page<Voluntario> voluntarios = service.filtrar(funcoes, genero, page, size);

        model.addAttribute("voluntarios", voluntarios);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        model.addAttribute("funcoesIds", funcoesIds);
        model.addAttribute("genero", genero);
        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);
        model.addAttribute("email", email);
        model.addAttribute("telefone", telefone);
        model.addAttribute("cep", cep);
        model.addAttribute("bairro", bairro);
        model.addAttribute("endereco", endereco);
        model.addAttribute("cidade", cidade);
        model.addAttribute("estado", estado);
        model.addAttribute("vazio", false);

        if (!temFuncao && !temGenero && nome == null && cpf == null && email == null && telefone == null && cep == null && bairro == null && endereco == null && cidade == null && estado == null) {
            return "redirect:/voluntario/listar";
        }

        if (voluntarios.isEmpty()){
            model.addAttribute("vazio", voluntarios.isEmpty());
        }

        return "administrativo/voluntario/filtro/lista";
    }

    @GetMapping("visualiza/{id}")
    public String visualizar(@PathVariable Long id,Model model) {
        model.addAttribute("voluntario", service.buscarPorId(id));
        return "administrativo/voluntario/visualizar";
    }

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        model.addAttribute("voluntario", service.buscarPorId(id));
        return "administrativo/voluntario/formulario";
    }

    @PostMapping("remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        service.deletarPorId(id);
        redirectAttributes.addFlashAttribute("sucesso", "Voluntario deletado com sucesso!");
        return "redirect:/voluntario/listar";
    }

    @GetMapping("listarUsuarioSimples")
    public String listarUsuarioSimples(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        Page<Voluntario> voluntarios = service.listarAtivos(page, size);

        model.addAttribute("voluntarios", voluntarios);
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        model.addAttribute("paginaAtual", page);
        return "usuario-simples/voluntario/lista";
    }

    @GetMapping("filtrarPesquisaUsuarioSimples")
    public String filtrarPesquisaUsuarioSimples(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<Voluntario> voluntarios = service.filtrarPesquisa("ATIVO", pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("voluntarios", voluntarios);
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", voluntarios.isEmpty());

            return "usuario-simples/voluntario/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/voluntario/listarUsuarioSimples";
        }
    }

    @GetMapping("filtrarUsuarioSimples")
    public String filtrarUsuarioSimples(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false, name = "funcoes") List<Long> funcoesIds,
            @RequestParam(required = false) Boolean nome,
            @RequestParam(required = false) Boolean cpf,
            @RequestParam(required = false) Boolean email,
            @RequestParam(required = false) Boolean telefone,
            @RequestParam(required = false) Boolean cep,
            @RequestParam(required = false) Boolean bairro,
            @RequestParam(required = false) Boolean endereco,
            @RequestParam(required = false) Boolean cidade,
            @RequestParam(required = false) Boolean estado,
            Model model) {

        List<FuncaoVoluntario> funcoes = (funcoesIds != null && !funcoesIds.isEmpty())
                ? funcaoVoluntarioService.buscarPorIds(funcoesIds)
                : Collections.emptyList();

        boolean temFuncao = !funcoes.isEmpty();
        boolean temGenero = (genero != null && !genero.isBlank());

        Page<Voluntario> voluntarios = service.filtrar(funcoes, genero, page, size);

        model.addAttribute("voluntarios", voluntarios);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        model.addAttribute("funcoesIds", funcoesIds);
        model.addAttribute("genero", genero);
        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);
        model.addAttribute("email", email);
        model.addAttribute("telefone", telefone);
        model.addAttribute("cep", cep);
        model.addAttribute("bairro", bairro);
        model.addAttribute("endereco", endereco);
        model.addAttribute("cidade", cidade);
        model.addAttribute("estado", estado);
        model.addAttribute("vazio", false);

        if (!temFuncao && !temGenero && nome == null && cpf == null && email == null && telefone == null && cep == null && bairro == null && endereco == null && cidade == null && estado == null) {
            return "redirect:/voluntario/listarUsuarioSimples";
        }

        if (voluntarios.isEmpty()){
            model.addAttribute("vazio", voluntarios.isEmpty());
        }

        return "usuario-simples/voluntario/filtro/lista";
    }

    @GetMapping("visualizaUsuarioSimples/{id}")
    public String visualizaUsuarioSimples(@PathVariable Long id,Model model) {
        model.addAttribute("voluntario", service.buscarPorId(id));
        return "usuario-simples/voluntario/visualizar";
    }
}

package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.FuncaoVoluntarioService;
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

    @GetMapping()
    public String formulario(Voluntario voluntario, Model model) {
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        return "voluntario/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Voluntario voluntario, BindingResult result, Model model) {
        if (voluntario.getFuncao() != null && voluntario.getFuncao().getId() != null &&
                voluntario.getFuncao().getId() == 0) {
            voluntario.setFuncao(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "voluntario/formulario";
        }

        try {
            service.salvar(voluntario);
            return "redirect:/voluntario/listar";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "voluntario/formulario";
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
        return "voluntario/lista";
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

            return "voluntario/pesquisaFiltro/lista";
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

        return "voluntario/filtro/lista";
    }

    @GetMapping("visualiza/{id}")
    public String visualizar(@PathVariable Long id,Model model) {
        model.addAttribute("voluntario", service.buscarPorId(id));
        return "voluntario/visualizar";
    }

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        model.addAttribute("voluntario", service.buscarPorId(id));
        return "voluntario/formulario";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id, Model model) {
        service.deletarPorId(id);
        return "redirect:/voluntario/listar";
    }
}

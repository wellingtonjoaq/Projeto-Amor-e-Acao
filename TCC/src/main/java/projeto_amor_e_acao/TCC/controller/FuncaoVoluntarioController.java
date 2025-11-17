package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.service.FuncaoVoluntarioService;
import projeto_amor_e_acao.TCC.service.UsuarioService;

import java.util.List;

@Controller
@RequestMapping("funcao")
public class FuncaoVoluntarioController {

    @Autowired
    private FuncaoVoluntarioService service;

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado() {
        return usuarioService.getUsuarioLogado();
    }

    @GetMapping()
    public String formulario(FuncaoVoluntario funcaoVoluntario, Model model) {
        model.addAttribute("funcao", new FuncaoVoluntario());
        return "administrativo/funcao/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid FuncaoVoluntario funcaoVoluntario, BindingResult result, Model model) {

        if (result.hasErrors()) {
            Usuario usuario = usuarioService.getUsuarioLogado();
            model.addAttribute("usuarioLogado", usuario);
            model.addAttribute("funcao", funcaoVoluntario);
            return "administrativo/funcao/formulario";
        }

        try {
            service.salvar(funcaoVoluntario);
            return "redirect:/funcao/listar";

        } catch (Exception e) {
            Usuario usuario = usuarioService.getUsuarioLogado();
            model.addAttribute("usuarioLogado", usuario);
            model.addAttribute("erro", e.getMessage());
            return "administrativo/funcao/lista";
        }
    }

    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("funcoes", service.listarTodos());
        return "administrativo/funcao/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            List<FuncaoVoluntario> funcoes = service.filtrarPesquisa(pesquisa);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("funcoes", funcoes);
            model.addAttribute("vazio", funcoes.isEmpty());

            Usuario usuario = usuarioService.getUsuarioLogado();
            model.addAttribute("usuarioLogado", usuario);

            return "administrativo/funcao/pesquisaFiltro/lista";
        }
        else {

            return "redirect:/funcao/listar";
        }
    }

    @GetMapping("visualiza/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("funcao", service.buscarPorId(id));
        return "administrativo/funcao/visualizar";
    }

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("funcao", service.buscarPorId(id));
        return "administrativo/funcao/formulario";
    }


    @PostMapping("remover/{id}")
    public String remover(@PathVariable Long id) {
        service.deletarPorId(id);
        return "redirect:/funcao/listar";
    }
}

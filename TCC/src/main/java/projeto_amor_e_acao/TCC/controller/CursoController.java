package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.service.CursoService;

@Controller
@RequestMapping("curso")
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping()
    public String formulario(Curso curso, Model model) {
        return "curso/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Curso curso, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("curso", curso);
            return "curso/formulario";
        }

        try {
            service.salvar(curso);
            return "redirect:/curso/listar";
        }
        catch (IllegalStateException e){
            model.addAttribute("erro", e.getMessage());
            return "curso/formulario";
        }
        catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("curso", curso);
            return "curso/formulario";
        }
    }

    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("cursos", service.listarTodos());
        return "curso/lista";
    }

    @GetMapping("visualiza/{id}")
    public String visualizar(@PathVariable Long id,Model model) {
        model.addAttribute("curso", service.buscarPorId(id));
        return "curso/visualizar";
    }

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", service.buscarPorId(id));
        return "curso/formulario";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id, Model model) {
        service.deletarPorId(id);
        return "redirect:/curso/listar";
    }
}

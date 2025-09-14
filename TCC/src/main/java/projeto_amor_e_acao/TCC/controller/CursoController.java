package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String iniciar(Curso curso, Model model) {
        return "curso/formulario";
    }

    @PostMapping()
    public String inserir(Curso curso, Model model) {
        return iniciar(curso, model);
    }

    @PostMapping("salvar")
    public String salvar(Curso curso, Model model) {
        try {
            service.salvar(curso);
            return "redirect:/curso/listar";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro: " + e.getMessage());
            return iniciar(curso, model);
        }
    }

    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("cursos", service.listarTodos());
        return "curso/lista";
    }

    @GetMapping("editar/{id}")
    public String alterar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", service.buscarPorId(id));
        return "curso/formulario";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id, Model model) {
        service.deletarPorId(id);
        return "redirect:/curso/listar";
    }
}

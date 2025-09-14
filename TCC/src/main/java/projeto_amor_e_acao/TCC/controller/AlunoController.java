package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.service.AlunoService;


@Controller
@RequestMapping("aluno")
public class AlunoController {

    @Autowired
    private AlunoService service;

    @GetMapping()
    public String iniciar(Aluno aluno, Model model) {
        return "aluno/formulario";
    }

    @PostMapping()
    public String inserir(Aluno aluno, Model model) {
        return iniciar(aluno, model);
    }

    @PostMapping("salvar")
    public String salvar(Aluno aluno, Model model) {
        try {
            service.salvar(aluno);
            return "redirect:/aluno/listar";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro: " + e.getMessage());
            return iniciar(aluno, model);
        }
    }

    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarTodos());
        return "aluno/lista";
    }

    @GetMapping("editar/{id}")
    public String alterar(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", service.buscarPorId(id));
        return "aluno/formulario";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id, Model model) {
        service.deletarPorId(id);
        return "redirect:/aluno/listar";
    }
}

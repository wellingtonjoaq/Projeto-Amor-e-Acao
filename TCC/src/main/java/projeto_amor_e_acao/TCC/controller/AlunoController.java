package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.Matricula;
import projeto_amor_e_acao.TCC.service.AlunoService;
import projeto_amor_e_acao.TCC.service.CursoService;
import java.util.List;


@Controller
@RequestMapping("aluno")
public class AlunoController {

    @Autowired
    private AlunoService service;

    @Autowired
    private CursoService cursoService;

    @GetMapping()
    public String formulario(Aluno aluno, Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "aluno/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid Aluno aluno,
            BindingResult result,
            @RequestParam("cursosSelecionados") List<Long> cursosIds,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("cursos", cursoService.listarTodos());
            model.addAttribute("aluno", aluno);
            return "aluno/formulario";
        }

        try {
            cursosIds.forEach(cursoId -> {
                Curso curso = cursoService.buscarPorId(cursoId);
                Matricula matricula = new Matricula();
                matricula.setCurso(curso);
                matricula.setAluno(aluno);
                aluno.getMatriculas().add(matricula);
            });

            service.salvar(aluno);
            return "redirect:/aluno/listar";

        }
        catch (IllegalStateException e){
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("cursos", cursoService.listarTodos());
            return "aluno/formulario";
        }
        catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("aluno", aluno);
            model.addAttribute("cursos", cursoService.listarTodos());
            return "aluno/formulario";
        }
    }


    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarAtivos());
        return "aluno/lista";
    }

    @GetMapping("visualiza/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", service.buscarPorId(id));
        return "aluno/visualizar";
    }

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", service.buscarPorId(id));
        model.addAttribute("cursos", cursoService.listarTodos());
        return "aluno/formulario";
    }

    @PostMapping("remover/{id}")
    public String remover(@PathVariable Long id) {
        service.deletarPorId(id);
        return "redirect:/aluno/listar";
    }
}


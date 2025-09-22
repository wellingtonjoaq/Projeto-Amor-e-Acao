package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.Matricula;
import projeto_amor_e_acao.TCC.service.AlunoService;
import projeto_amor_e_acao.TCC.service.CursoService;
import java.util.ArrayList;
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

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Aluno aluno = service.buscarPorId(id);
        model.addAttribute("aluno", aluno);
        model.addAttribute("cursos", cursoService.listarTodos());
        return "aluno/formulario";
    }

    @PostMapping("salvar")
    public String salvar(Aluno aluno, @RequestParam("cursosSelecionados") List<Long> cursosIds, Model model) {
        try {
            aluno.setMatriculas(new ArrayList<>());

            for (Long cursoId : cursosIds) {
                Curso curso = cursoService.buscarPorId(cursoId);
                Matricula matricula = new Matricula();
                matricula.setAluno(aluno);
                matricula.setCurso(curso);
                aluno.getMatriculas().add(matricula);
            }

            service.salvar(aluno);

            return "redirect:/aluno/listar";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro: " + e.getMessage());
            model.addAttribute("cursos", cursoService.listarTodos());
            return "aluno/formulario";
        }
    }


    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarTodos());
        return "aluno/lista";
    }

    @GetMapping("vizualiza/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", service.buscarPorId(id));
        model.addAttribute("modo", "visualizar");
        return "aluno/vizualizar";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id) {
        service.deletarPorId(id);
        return "redirect:/aluno/listar";
    }
}


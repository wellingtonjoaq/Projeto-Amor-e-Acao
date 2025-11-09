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
import projeto_amor_e_acao.TCC.model.Matricula;
import projeto_amor_e_acao.TCC.service.AlunoService;
import projeto_amor_e_acao.TCC.service.CursoService;

import java.util.Collections;
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
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        Page<Aluno> alunos = service.listarAtivos(page, size);

        model.addAttribute("alunos", alunos);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("cursos", cursoService.listarTodos());
        return "aluno/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<Aluno> alunos = service.filtrarPesquisa("ATIVO", pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("alunos", alunos);
            model.addAttribute("paginaAtual", page);
            model.addAttribute("cursos", cursoService.listarTodos());
            model.addAttribute("vazio", alunos.isEmpty());

            return "aluno/pesquisaFiltro/lista";
        }
        else {

            return "redirect:/aluno/listar";
        }
    }


    @GetMapping("filtrar")
    public String filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false, name = "cursos") List<Long> cursosIds,
            @RequestParam(required = false) Boolean nome,
            @RequestParam(required = false) Boolean cpf,
            @RequestParam(required = false) Boolean email,
            @RequestParam(required = false) Boolean telefone,
            @RequestParam(required = false) Boolean cep,
            @RequestParam(required = false) Boolean bairro,
            @RequestParam(required = false) Boolean endereco,
            Model model) {

        List<Curso> cursos = (cursosIds != null && !cursosIds.isEmpty())
                ? cursoService.buscarPorIds(cursosIds)
                : Collections.emptyList();

        boolean temCursos = !cursos.isEmpty();
        boolean temGenero = (genero != null && !genero.isBlank());

        Page<Aluno> alunos = service.filtrar(cursos, genero, page, size);

        model.addAttribute("alunos", alunos);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("cursos", cursoService.listarTodos());
        model.addAttribute("cursosIds", cursosIds);
        model.addAttribute("genero", genero);
        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);
        model.addAttribute("email", email);
        model.addAttribute("telefone", telefone);
        model.addAttribute("cep", cep);
        model.addAttribute("bairro", bairro);
        model.addAttribute("endereco", endereco);
        model.addAttribute("vazio", false);

        if (!temCursos && !temGenero && nome == null && cpf == null && email == null && telefone == null && cep == null && bairro == null && endereco == null) {
            return "redirect:/aluno/listar";
        }

        if (alunos.isEmpty()){
            model.addAttribute("vazio", alunos.isEmpty());
        }

        return "aluno/filtro/lista";
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


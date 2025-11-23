package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.model.*;
import projeto_amor_e_acao.TCC.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("visitantes")
public class VisitantesController {

    @Autowired
    private FuncaoVoluntarioService funcaoVoluntarioService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private AlunoService alunoService;

    @GetMapping("/")
    public String index() {
        return "visitantes/index";
    }

    @GetMapping("cursos")
    public String curso(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "12") int size,
                        Model model) {

        Page<Curso> cursos = cursoService.listarPaginados(page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);
        return "visitantes/curso/lista";
    }

    @GetMapping("cursos/verTodos")
    public String verTodos(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "12") int size,
                        Model model) {

        Page<Curso> cursos = cursoService.listarPaginados(page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);
        return "visitantes/curso/verTodos/lista";
    }

    @GetMapping("cursos/filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<Curso> cursos = cursoService.filtrarPesquisa(pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("cursos", cursos);
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", cursos.isEmpty());

            return "visitantes/curso/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/visitantes/cursos/verTodos";
        }
    }

    @GetMapping("cursos/filtrar")
    public String filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String periodo,
            Model model) {

        boolean temCategoria = (categoria != null && !categoria.isEmpty());
        boolean temPeriodo = (periodo != null && !periodo.isEmpty());
        boolean temStatus = (status != null && !status.isEmpty());

        Page<Curso> cursos = cursoService.filtrar(categoria, periodo, status, page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("categoria", categoria);
        model.addAttribute("status", status);
        model.addAttribute("periodo", periodo);
        model.addAttribute("vazio", false);

        if (!temCategoria && !temStatus && !temPeriodo) {
            return "redirect:/visitantes/curso/verTodos";
        }

        if (cursos.isEmpty()){
            model.addAttribute("vazio", cursos.isEmpty());
        }


        return "visitantes/curso/filtro/lista";
    }

    @GetMapping("doacoes")
    public String doacoes() {
        return "visitantes/doacoes/lista";
    }

    @GetMapping("sobre")
    public String sobre() {
        return "visitantes/sobreNos/lista";
    }

    @GetMapping("voluntario")
    public String voluntario(Voluntario voluntario, Model model) {
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        return "visitantes/voluntario/formulario";
    }

    @PostMapping("voluntario/salvar")
    public String salvar(@Valid Voluntario voluntario, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (voluntario.getFuncao() != null && voluntario.getFuncao().getId() != null &&
                voluntario.getFuncao().getId() == 0) {
            voluntario.setFuncao(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "visitantes/voluntario/formulario";
        }

        try {
            voluntario.setStatus("PENDENT");
            voluntarioService.salvar(voluntario);
            redirectAttributes.addFlashAttribute("sucesso", "Formulario enviado com sucesso!");
            return "redirect:/voluntario/listar";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "visitantes/voluntario/formulario";
        }
    }

    @GetMapping("matricula")
    public String matricula(Aluno aluno, Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "visitantes/matricula/formulario";
    }

    @PostMapping("matricula/salvar")
    public String salvar(
            @Valid Aluno aluno,
            BindingResult result,
            @RequestParam(value = "cursosSelecionados", required = false) List<Long> cursosIds,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("cursos", cursoService.listarTodos());
            model.addAttribute("aluno", aluno);
            return "visitantes/matricula/formulario";
        }

        if (cursosIds == null || cursosIds.isEmpty()) {
            model.addAttribute("cursos", cursoService.listarTodos());
            model.addAttribute("aluno", aluno);
            model.addAttribute("matriculaErro", "( Selecione pelo menos um curso! )");
            return "visitantes/matricula/formulario";
        }

        try {
            cursosIds.forEach(cursoId -> {
                Curso curso = cursoService.buscarPorId(cursoId);
                Matricula matricula = new Matricula();
                matricula.setCurso(curso);
                matricula.setAluno(aluno);
                aluno.getMatriculas().add(matricula);
            });

            aluno.setStatus("PENDENT");

            alunoService.salvar(aluno);
            return "redirect:/visitantes/cursos";
        }
        catch (IllegalStateException e) {
            if (e.getMessage().contains("CPF")) {
                result.rejectValue("cpf", "error.aluno", e.getMessage());
            } else if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.aluno", e.getMessage());
            }

            model.addAttribute("cursos", cursoService.listarTodos());
            return "visitantes/matricula/formulario";
        } catch (Exception e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("aluno", aluno);
            model.addAttribute("cursos", cursoService.listarTodos());
            return "visitantes/matricula/formulario";
        }
    }


    @GetMapping("parceria")
    public String parceria(Model model) {
        model.addAttribute("empresaParceira", new EmpresaParceira());
        return "visitantes/parceria/formulario";
    }

    @PostMapping("/parceria/salvar")
    public String salvar(
            @Valid @ModelAttribute("empresaParceira") EmpresaParceira empresaParceira,
            BindingResult result, Model model, RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors()) {
            return "visitantes/parceria/formulario";
        }

        try {
            empresaParceira.setStatus("PENDENT");
            empresaParceiraService.salvar(empresaParceira);
            redirectAttributes.addFlashAttribute("sucesso", "Formulario enviado com sucesso!");
            return "redirect:/visitantes/parceria";
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("CPF")) {
                result.rejectValue("cpfRepresentante", "error.empresaParceira", e.getMessage());
            } else if (e.getMessage().contains("CNPJ")) {
                result.rejectValue("cnpj", "error.empresaParceira", e.getMessage());
            } else if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.empresaParceira", e.getMessage());
            }

            return "visitantes/parceira/formulario";
        } catch (Exception e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("empresaParceira", empresaParceira);
            return "visitantes/parceira/formulario";
        }
    }

    @GetMapping("login")
    public String login() {
        return "redirect:/login";
    }
}

package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.service.CursoService;
import projeto_amor_e_acao.TCC.service.FirebaseStorageService;

import java.io.IOException;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("curso")
public class CursoController {

    @Autowired
    private CursoService service;

    @Autowired
    private FirebaseStorageService firebaseService;

    @GetMapping()
    public String formulario(Curso curso, Model model) {
        model.addAttribute("curso", curso);
        return "curso/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Curso curso,
                         BindingResult result,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("curso", curso);
            return "curso/formulario";
        }

        try {
            if (file != null && !file.isEmpty()) {
                String url = firebaseService.uploadFile(file);
                curso.setFoto(url);
            }

            service.salvar(curso);
            return "redirect:/curso/listar";
        }
        catch (IllegalStateException e) {
            model.addAttribute("erro", e.getMessage());
            return "curso/formulario";
        }
        catch (IOException e) {
            model.addAttribute("erro", "Erro ao enviar imagem: " + e.getMessage());
            return "curso/formulario";
        }
        catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("curso", curso);
            return "curso/formulario";
        }
    }

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "12") int size,
                         Model model) {

        Page<Curso> cursos = service.listarPaginados(page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);

        return "curso/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        Page<Curso> cursos = service.filtrarPesquisa(pesquisa, page, size);

        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("vazio", cursos.isEmpty());

        return "curso/pesquisaFiltro/lista";
    }

//    @GetMapping("filtrar")
//    public String filtrar(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false, name = "categoriasSelecionadas") List<String> categoria,
//            Model model) {
//
//        boolean temCategoria = (categoria != null && !categoria.isEmpty());
//        boolean temStatus = (status != null && !status.isBlank() && !status.equalsIgnoreCase("TODOS"));
//
//        Page<Curso> cursos = service.filtrar(docentes, status, page, size);
//
//        model.addAttribute("cursos", cursos);
//        model.addAttribute("paginaAtual", page);
//        model.addAttribute("docentes", docentes);
//        model.addAttribute("status", status);
//        model.addAttribute("vazio", false);
//
//        if (!temdocente && !temStatus) {
//            model.addAttribute("vazio", true);
//        }
//
//        if (cursos.isEmpty()){
//            model.addAttribute("vazio", cursos.isEmpty());
//        }
//
//        return "curso/filtro/lista";
//    }

    @GetMapping("visualiza/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", service.buscarPorId(id));
        return "curso/visualizar";
    }

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", service.buscarPorId(id));
        return "curso/formulario";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id) {
        service.deletarPorId(id);
        return "redirect:/curso/listar";
    }
}

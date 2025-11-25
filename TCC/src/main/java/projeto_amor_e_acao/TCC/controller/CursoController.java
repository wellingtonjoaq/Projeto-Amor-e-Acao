package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.service.CursoService;
import projeto_amor_e_acao.TCC.service.FirebaseStorageService;
import projeto_amor_e_acao.TCC.service.NotificacaoService;
import projeto_amor_e_acao.TCC.service.UsuarioService;

import java.io.IOException;

import java.util.List;

@Controller
@RequestMapping("curso")
public class CursoController {

    @Autowired
    private CursoService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FirebaseStorageService firebaseService;

    @Autowired
    private NotificacaoService notificacaoService;

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado() {
        return usuarioService.getUsuarioLogado();
    }

    @ModelAttribute("notificacoesMenu")
    public List<NotificacaoDTO> carregarNotifMenu() {
        return notificacaoService.listarNotificacaoLimitado(7);
    }

    @GetMapping()
    public String formulario(Curso curso, Model model) {
        model.addAttribute("curso", curso);
        return "administrativo/curso/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Curso curso,
                         BindingResult result,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "categoriasSelecionadas", required = false) List<String> categoriasSelecionadas,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("curso", curso);
            return "administrativo/curso/formulario";
        }

        if (curso.getId() != null) {
            var cursoExistente = service.buscarPorId(curso.getId());

            if (file != null && !file.isEmpty()) {
                if (cursoExistente.getFoto() != null && !cursoExistente.getFoto().isBlank()) {
                    firebaseService.deleteFile(cursoExistente.getFoto());
                }
            } else {
                curso.setFoto(cursoExistente.getFoto());
            }
        }

        if (categoriasSelecionadas == null || categoriasSelecionadas.isEmpty()) {
            model.addAttribute("curso", curso);
            model.addAttribute("erro", "Selecione pelo menos uma categoria.");
            return "administrativo/curso/formulario";
        }

        boolean novo = (curso.getId() == null);

        try {
            if (file != null && !file.isEmpty()) {
                String url = firebaseService.uploadFile(file);
                curso.setFoto(url);
            }

            if (categoriasSelecionadas.size() > 3) {
                categoriasSelecionadas = categoriasSelecionadas.subList(0, 3);
            }

            curso.setCategorias(String.join(",", categoriasSelecionadas));

            service.salvar(curso);
            if (novo){
                redirectAttributes.addFlashAttribute("sucesso", "Curso salvo com sucesso!");
            }
            else {
                redirectAttributes.addFlashAttribute("sucesso", "Curso atualizado com sucesso!");
            }
            return "redirect:/curso/listar";

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao fazer upload da imagem", e);

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("curso", curso);
            return "administrativo/curso/formulario";
        }
    }


    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "12") int size,
                         Model model) {

        Page<Curso> cursos = service.listarPaginados(page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);

        return "administrativo/curso/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<Curso> cursos = service.filtrarPesquisa(pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("cursos", cursos);
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", cursos.isEmpty());

            return "administrativo/curso/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/curso/listar";
        }
    }

    @GetMapping("filtrar")
    public String filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String periodo,
            Model model) {

        boolean temPeriodo = (periodo != null
                && !periodo.isBlank());

        boolean temStatus = (status != null
                && !status.isBlank());

        boolean temCategoria = (categoria != null
                && !categoria.isBlank());


        Page<Curso> cursos = service.filtrar(categoria, periodo, status, page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("categoria", categoria);
        model.addAttribute("status", status);
        model.addAttribute("periodo", periodo);
        model.addAttribute("vazio", false);

        if (!temCategoria && !temStatus && !temPeriodo) {
            return "redirect:/curso/listar";
        }

        if (cursos.isEmpty()){
            model.addAttribute("vazio", cursos.isEmpty());
        }

        return "administrativo/curso/filtro/lista";
    }

    @GetMapping("editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", service.buscarPorId(id));
        return "administrativo/curso/formulario";
    }

    @PostMapping("remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.deletarPorId(id);
        redirectAttributes.addFlashAttribute("sucesso", "Curso deletado com sucesso!");
        return "redirect:/curso/listar";
    }

    @GetMapping("listarUsuarioSimples")
    public String listarUsuarioSimples(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "12") int size,
                         Model model) {

        Page<Curso> cursos = service.listarPaginados(page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);

        return "usuario-simples/curso/lista";
    }

    @GetMapping("filtrarPesquisaUsuarioSimples")
    public String filtrarPesquisaUsuarioSimples(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<Curso> cursos = service.filtrarPesquisa(pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("cursos", cursos);
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", cursos.isEmpty());

            return "usuario-simples/curso/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/curso/listarUsuarioSimples";
        }
    }

    @GetMapping("filtrarUsuarioSimples")
    public String filtrarUsuarioSimples(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String periodo,
            Model model) {

        boolean temCategoria = (categoria != null && !categoria.isEmpty());
        boolean temPeriodo = (periodo != null && !periodo.isEmpty());
        boolean temStatus = (status != null && !status.isEmpty());

        Page<Curso> cursos = service.filtrar(categoria, periodo, status, page, size);

        model.addAttribute("cursos", cursos);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("categoria", categoria);
        model.addAttribute("status", status);
        model.addAttribute("periodo", periodo);
        model.addAttribute("vazio", false);

        if (!temCategoria && !temStatus && !temPeriodo) {
            return "redirect:/curso/listarUsuarioSimples";
        }

        if (cursos.isEmpty()){
            model.addAttribute("vazio", cursos.isEmpty());
        }

        return "usuario-simples/curso/filtro/lista";
    }
}

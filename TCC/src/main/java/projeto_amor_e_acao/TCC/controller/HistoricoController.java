package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.dto.HistoricoDTO;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.*;

import java.util.Optional;

@Controller
@RequestMapping("historico")
public class HistoricoController {

    @Autowired
    private HistoricoService service;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        Page<HistoricoDTO> historicos = service.listarHistorico(page, size);
        model.addAttribute("historicos", historicos);
        return "historico/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (pesquisa != null && !pesquisa.isEmpty()){
            Page<HistoricoDTO> historicos = service.filtrarPesquisaHistorico(pesquisa,page, size);
            model.addAttribute("historicos", historicos);
            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("vazio", historicos.isEmpty());

            return "historico/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/historico/listar";
        }
    }

    @GetMapping("visualizaAluno/{id}")
    public String visualizarAluno(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", alunoService.buscarPorId(id));
        return "historico/visualizarAluno";
    }

    @GetMapping("visualizaVoluntario/{id}")
    public String visualizarVoluntario(@PathVariable Long id, Model model) {
        model.addAttribute("voluntario", voluntarioService.buscarPorId(id));
        return "historico/visualizarVoluntario";
    }

    @GetMapping("visualizaEmpresaParceira/{id}")
    public String visualizarEmpresaParceira(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.findById(id);

            model.addAttribute("empresa", empresaParceira.get());
            return "historico/visualizarEmpresaParceira";
    }

    @GetMapping("visualizaUsuario/{id}")
    public String visualizarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);

            model.addAttribute("usuario", usuarioOptional.get());
            return "usuario/visualizar";
    }

    @PostMapping("/ativarAluno/{id}")
    public String ativarAluno(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);

        aluno.setStatus("ATIVO");
        alunoService.salvar(aluno);

        return "redirect:/historico/listar";
    }

    @PostMapping("ativarVoluntario/{id}")
    public String ativarVoluntario(@PathVariable Long id) {
        Voluntario voluntario = voluntarioService.buscarPorId(id);

        voluntario.setStatus("ATIVO");
        voluntarioService.salvar(voluntario);

        return "redirect:/historico/listar";
    }

    @PostMapping("/ativarEmpresaParceira/{id}")
    public String ativarEmpresaParceira(@PathVariable Long id) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.findById(id);

        if (empresaParceira.isPresent()) {
            EmpresaParceira ep = empresaParceira.get();
            ep.setStatus("ATIVO");
            empresaParceiraService.save(ep);
        }

        return "redirect:/historico/listar";
    }


    @PostMapping("ativarUsuario/{id}")
    public String ativarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);

        usuario.get().setStatus("ATIVO");
        usuarioService.save(usuario.orElse(null));

        return "redirect:/historico/listar";
    }
}

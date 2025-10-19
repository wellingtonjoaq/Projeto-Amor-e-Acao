package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.*;

import java.util.List;

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
    public String listar(Model model) {
        model.addAttribute("historicos", service.listarHistorico());
        return "historico/lista";
    }

    @GetMapping("visualizaAluno/{id}")
    public String visualizarAluno(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", alunoService.buscarPorId(id));
        return "historico/visualizar";
    }

    @GetMapping("visualizaVoluntario/{id}")
    public String visualizarVoluntario(@PathVariable Long id, Model model) {
        model.addAttribute("voluntario", voluntarioService.buscarPorId(id));
        return "historico/visualizar";
    }

    @GetMapping("visualizaEmpresaParceira/{id}")
    public String visualizarEmpresaParceira(@PathVariable Long id, Model model) {
        model.addAttribute("empresaParceira", empresaParceiraService.findById(id));
        return "historico/visualizar";
    }

    @GetMapping("visualizaUsuario/{id}")
    public String visualizarUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.findById(id));
        return "historico/visualizar";
    }

    @PostMapping("ativarAluno")
    public String ativarAluno(Aluno aluno, Model model) {
        aluno.setStatus("ATIVO");
        alunoService.salvar(aluno);
        return "redirect:/historico/listar";
    }

    @PostMapping("ativarVoluntario/{id}")
    public String ativarVoluntario(Voluntario voluntario, Model model) {
        voluntario.setStatus("ATIVO");
        voluntarioService.salvar(voluntario);
        return "redirect:/historico/listar";
    }

    @PostMapping("ativarEmpresaParceira/{id}")
    public String ativarEmpresaParceira(EmpresaParceira empresaParceira, Model model) {
        empresaParceira.setStatus(EmpresaParceira.Status.ATIVO);
        empresaParceiraService.save(empresaParceira);
        return "redirect:/historico/listar";
    }

    @PostMapping("ativarUsuario/{id}")
    public String ativarUsuario(Usuario usuario, Model model) {
        usuario.setStatus(Usuario.Status.ATIVO);
        usuarioService.save(usuario);
        return "redirect:/historico/listar";
    }
}

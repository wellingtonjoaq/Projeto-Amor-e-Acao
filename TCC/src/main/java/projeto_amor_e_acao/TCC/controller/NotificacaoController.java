package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.*;

import java.util.Optional;

@Controller
@RequestMapping("notificacao")
public class NotificacaoController {

    @Autowired
    private NotificacaoService service;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size, Model model) {
        model.addAttribute("notificacoes", service.listarNotificacao(page, size));
        model.addAttribute("paginaAtual", page);
        return "notificacao/lista";
    }

    @GetMapping("visualizaAluno/{id}")
    public String visualizarAluno(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", alunoService.buscarPorId(id));
        return "notificacao/visualizarAluno";
    }

    @GetMapping("visualizaVoluntario/{id}")
    public String visualizarVoluntario(@PathVariable Long id, Model model) {
        model.addAttribute("voluntario", voluntarioService.buscarPorId(id));
        return "notificacao/visualizarVoluntario";
    }

    @GetMapping("visualizaEmpresaParceira/{id}")
    public String visualizarEmpresaParceira(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.buscarPorId(id);

        model.addAttribute("empresa", empresaParceira.get());
        return "notificacao/visualizarEmpresaParceira";
    }

    @PostMapping("/ativarAluno/{id}")
    public String ativarAluno(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);

        aluno.setStatus("ATIVO");
        alunoService.salvar(aluno);

        return "redirect:/notificacao/listar";
    }

    @PostMapping("ativarVoluntario/{id}")
    public String ativarVoluntario(@PathVariable Long id) {
        Voluntario voluntario = voluntarioService.buscarPorId(id);

        voluntario.setStatus("ATIVO");
        voluntarioService.salvar(voluntario);

        return "redirect:/notificacao/listar";
    }

    @PostMapping("/ativarEmpresaParceira/{id}")
    public String ativarEmpresaParceira(@PathVariable Long id) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.buscarPorId(id);

        if (empresaParceira.isPresent()) {
            EmpresaParceira ep = empresaParceira.get();
            ep.setStatus("ATIVO");
            empresaParceiraService.salvar(ep);
        }

        return "redirect:/notificacao/listar";
    }

    @PostMapping("/recusarAluno/{id}")
    public String recusarAluno(@PathVariable Long id) {
        alunoService.deletarPorId(id);
        return "redirect:/notificacao/listar";
    }

    @PostMapping("recusarVoluntario/{id}")
    public String recusarVoluntario(@PathVariable Long id) {
        voluntarioService.deletarPorId(id);
        return "redirect:/notificacao/listar";
    }

    @PostMapping("/recusarEmpresaParceira/{id}")
    public String recusarEmpresaParceira(@PathVariable Long id) {
        empresaParceiraService.deletarPorId(id);
        return "redirect:/notificacao/listar";
    }
}

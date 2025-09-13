package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.service.EmpresaParceiraService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/empresasParceiras")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @GetMapping
    public String listarEmpresasParceiras(Model model) {
        List<EmpresaParceira> empresasParceiras = empresaParceiraService.findAll();
        model.addAttribute("empresasParceiras", empresasParceiras);
        return "empresasParceiras/listar";
    }

    @GetMapping("/novo")
    public String novoEmpresaParceira(Model model) {
        model.addAttribute("empresaParceira", new EmpresaParceira());
        return "empresasParceiras/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarEmpresaParceira(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.findById(id);

        if (empresaParceira.isPresent()) {
            model.addAttribute("empresaParceira", empresaParceira.get());
            return "empresasParceiras/formulario";
        } else {
            return "redirect:/empresasParceiras";
        }
    }

    @PostMapping
    public String salvarEmpresaParceira(@ModelAttribute @Valid EmpresaParceira empresaParceira,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("empresaParceira", empresaParceira);
            return "empresasParceiras/formulario";
        }

        empresaParceiraService.save(empresaParceira);
        return "redirect:/empresasParceiras";
    }

    @GetMapping("/deletar/{id}")
    public String deletarEmpresaParceira(@PathVariable Long id) {
        empresaParceiraService.deleteById(id);
        return "redirect:/empresasParceiras";
    }
}

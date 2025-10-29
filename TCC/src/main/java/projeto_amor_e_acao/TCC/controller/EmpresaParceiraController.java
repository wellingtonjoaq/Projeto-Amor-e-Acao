package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.model.Aluno;
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
    public String listarEmpresasParceiras(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size,
                                          Model model) {

        Page<EmpresaParceira> empresasParceiras = empresaParceiraService.listarAtivos(page, size);
        model.addAttribute("empresasParceiras", empresasParceiras);
        model.addAttribute("paginaAtual", page);

        return "empresaParceira/listar";
    }

    @GetMapping("/novo")
    public String novoEmpresaParceira(Model model) {
        model.addAttribute("empresaParceira", new EmpresaParceira());
        return "empresaParceira/formulario";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizarEmpresaParceira(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.findById(id);

        if (empresaParceira.isPresent()) {
            model.addAttribute("empresa", empresaParceira.get());
            return "empresaParceira/visualizar";
        } else {
            model.addAttribute("errorMessage",
                    "Empresa parceira não encontrada.");
            return "redirect:/empresasParceiras";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarEmpresaParceira(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = empresaParceiraService.findById(id);

        if (empresaParceira.isPresent()) {
            model.addAttribute("empresaParceira", empresaParceira.get());
            return "empresaParceira/formulario";
        } else {
            model.addAttribute("errorMessage",
                    "Empresa parceira não encontrada.");
            return "redirect:/empresasParceiras";
        }
    }

    @PostMapping
    public String salvarEmpresaParceira(
            @Valid @ModelAttribute("empresaParceira") EmpresaParceira empresaParceira,
            BindingResult result, Model model, RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors()) {
            return "empresaParceira/formulario";
        }

        try {
            empresaParceiraService.save(empresaParceira);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Empresa parceira salva com sucesso!");

            return "redirect:/empresasParceiras";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("CNPJ")) {
                result.rejectValue(
                        "cnpj", "error.cnpj", e.getMessage());
            } else if (e.getMessage().contains("data de fim")) {
                result.rejectValue(
                        "data_fim", "error.data_fim", e.getMessage());
            } else if (e.getMessage().contains("E-mail")) {
                result.rejectValue(
                        "email", "error.email", e.getMessage());
            } else if (e.getMessage().contains("data de início")) {
                result.rejectValue(
                        "data_inicio", "error.data_inicio", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }

            return "empresaParceira/formulario";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "empresaParceira/formulario";
        }
    }

    @DeleteMapping("/{id}")
    public String deletarEmpresaParceira(@PathVariable Long id,
                                         RedirectAttributes redirectAttributes) {
        try {
            empresaParceiraService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Empresa parceira excluída com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());
        }

        return "redirect:/empresasParceiras";
    }
}

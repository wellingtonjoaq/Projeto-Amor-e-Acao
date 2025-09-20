package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            model.addAttribute("errorMessage",
                    "Empresa parceira não encontrada.");
            return "redirect:/empresasParceiras";
        }
    }

    @PostMapping
    public String salvarEmpresaParceira(
            @Valid @ModelAttribute("empresaParceira") EmpresaParceira empresaParceira,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        Optional<EmpresaParceira> exist =
                empresaParceiraService.findByCnpj(empresaParceira.getCnpj());

        if (exist.isPresent() && !exist.get().getId().equals(empresaParceira.getId())) {
            result.rejectValue("cnpj", "error.cnpj",
                    "Já existe uma empresa cadastrada com este CNPJ.");
        }

        if (empresaParceira.getData_fim() != null &&
                empresaParceira.getData_inicio() != null &&
                empresaParceira.getData_fim().isBefore(
                empresaParceira.getData_inicio().toLocalDate())) {
            result.rejectValue("data_fim", "error.data_fim",
                    "A data de fim não pode ser anterior à data de início.");
        }

        if (result.hasErrors()) {
            return "empresasParceiras/formulario";
        }

        try {
            empresaParceiraService.save(empresaParceira);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Empresa parceira salva com sucesso!");

            return "redirect:/empresasParceiras";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "empresasParceiras/formulario";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarEmpresaParceira(@PathVariable Long id,
                                         RedirectAttributes redirectAttributes) {
        try {
            empresaParceiraService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Empresa parceira excluída com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/empresasParceiras";
    }
}



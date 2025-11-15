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
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.service.EmpresaParceiraService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("empresaParceira")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraService service;

    @GetMapping()
    public String formulario(Model model) {
        model.addAttribute("empresaParceira", new EmpresaParceira());
        return "administrativo/empresaParceira/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("empresaParceira") EmpresaParceira empresaParceira,
            BindingResult result, Model model, RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors()) {
            return "administrativo/empresaParceira/formulario";
        }

        try {
            service.salvar(empresaParceira);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Empresa parceira salva com sucesso!");

            return "redirect:/empresaParceira/listar";
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("CPF")) {
                result.rejectValue("cpfRepresentante", "error.empresaParceira", e.getMessage());
            } else if (e.getMessage().contains("CNPJ")) {
                result.rejectValue("cnpj", "error.empresaParceira", e.getMessage());
            } else if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.empresaParceira", e.getMessage());
            }

            return "administrativo/empresaParceira/formulario";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("empresaParceira", empresaParceira);
            return "administrativo/empresaParceira/formulario";
        }
    }

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        Page<EmpresaParceira> empresasParceiras = service.listarAtivos(page, size);
        model.addAttribute("empresasParceiras", empresasParceiras);
        model.addAttribute("paginaAtual", page);

        return "administrativo/empresaParceira/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<EmpresaParceira> empresasParceiras = service.filtrarPesquisa("ATIVO", pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("empresasParceiras", empresasParceiras);
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", empresasParceiras.isEmpty());

            return "administrativo/empresaParceira/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/empresaParceira/listar";
        }
    }

    @GetMapping("filtrar")
    public String filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean nome,
            @RequestParam(required = false) Boolean cnpj,
            @RequestParam(required = false) Boolean email,
            @RequestParam(required = false) Boolean telefone,
            @RequestParam(required = false) Boolean nomeRepresentante,
            @RequestParam(required = false) Boolean cpfRepresentante,
            Model model) {

        Page<EmpresaParceira> empresasParceiras = service.listarAtivos(page, size);

        model.addAttribute("empresasParceiras", empresasParceiras);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("nome", nome);
        model.addAttribute("cnpj", cnpj);
        model.addAttribute("email", email);
        model.addAttribute("telefone", telefone);
        model.addAttribute("nomeRepresentante", nomeRepresentante);
        model.addAttribute("cpfRepresentante", cpfRepresentante);
        model.addAttribute("vazio", false);

        if (nome == null && cnpj == null && email == null && telefone == null && nomeRepresentante == null && cpfRepresentante == null) {
            return "redirect:/empresaParceira/listar";
        }

        if (empresasParceiras.isEmpty()){
            model.addAttribute("vazio", empresasParceiras.isEmpty());
        }

        return "administrativo/empresaParceira/filtro/lista";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = service.buscarPorId(id);

        if (empresaParceira.isPresent()) {
            model.addAttribute("empresa", empresaParceira.get());
            return "administrativo/empresaParceira/visualizar";
        } else {
            model.addAttribute("errorMessage",
                    "Empresa parceira não encontrada.");
            return "redirect:/empresaParceira/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<EmpresaParceira> empresaParceira = service.buscarPorId(id);

        if (empresaParceira.isPresent()) {
            model.addAttribute("empresaParceira", empresaParceira.get());
            return "administrativo/empresaParceira/formulario";
        } else {
            model.addAttribute("errorMessage",
                    "Empresa parceira não encontrada.");
            return "redirect:/empresaParceira/listar";
        }
    }

    @PostMapping("remover/{id}")
    public String remover(@PathVariable Long id,
                                         RedirectAttributes redirectAttributes) {
        try {
            service.deletarPorId(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Empresa parceira excluída com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());
        }

        return "redirect:/empresaParceira/listar";
    }
}

package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.VoluntarioService;

@Controller
@RequestMapping("voluntario")
public class VoluntarioController {

    @Autowired
    private VoluntarioService service;

    @GetMapping()
    public String formulario(Voluntario voluntario, Model model) {
        return "voluntario/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Voluntario voluntario, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("voluntario", voluntario);
            return "voluntario/formulario";
        }

        try {
            service.salvar(voluntario);
            return "redirect:/voluntario/listar";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("voluntario", voluntario);
            return "voluntario/formulario";
        }
    }

    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("voluntarios", service.listarTodos());
        return "voluntario/lista";
    }

    @GetMapping("visualiza/{id}")
    public String vizualizar(@PathVariable Long id,Model model) {
        model.addAttribute("voluntario", service.buscarPorId(id));
        model.addAttribute("modo", "visualizar");
        return "voluntario/vizualizar";
    }

    @GetMapping("editar/{id}")
    public String alterar(@PathVariable Long id, Model model) {
        model.addAttribute("voluntario", service.buscarPorId(id));
        return "voluntario/formulario";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id, Model model) {
        service.deletarPorId(id);
        return "redirect:/voluntario/listar";
    }
}

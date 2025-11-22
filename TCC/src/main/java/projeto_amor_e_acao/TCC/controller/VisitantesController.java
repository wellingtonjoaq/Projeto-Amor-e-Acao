package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.EmpresaParceiraService;
import projeto_amor_e_acao.TCC.service.FuncaoVoluntarioService;
import projeto_amor_e_acao.TCC.service.VoluntarioService;

@Controller
@RequestMapping("visitantes")
public class VisitantesController {

    @Autowired
    private FuncaoVoluntarioService funcaoVoluntarioService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private EmpresaParceiraService empresaParceiraService;

    @GetMapping("/")
    public String index() {
        return "visitantes/index";
    }

    @GetMapping("cursos")
    public String curso() {
        return "visitantes/curso/lista";
    }

    @GetMapping("doacoes")
    public String doacoes() {
        return "visitantes/doacoes/lista";
    }

    @GetMapping("sobre")
    public String sobre() {
        return "visitantes/sobreNos/lista";
    }

    @GetMapping("voluntario")
    public String voluntario(Voluntario voluntario, Model model) {
        model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
        return "visitantes/voluntario/formulario";
    }

    @PostMapping("voluntario/salvar")
    public String salvar(@Valid Voluntario voluntario, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (voluntario.getFuncao() != null && voluntario.getFuncao().getId() != null &&
                voluntario.getFuncao().getId() == 0) {
            voluntario.setFuncao(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "visitantes/voluntario/formulario";
        }

        try {
            voluntario.setStatus("PENDENT");
            voluntarioService.salvar(voluntario);
            redirectAttributes.addFlashAttribute("sucesso", "Formulario enviado com sucesso!");
            return "redirect:/voluntario/listar";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("funcoesVoluntario", funcaoVoluntarioService.listarTodos());
            return "visitantes/voluntario/formulario";
        }
    }

    @GetMapping("matricula")
    public String matricula() {
        return "visitantes/matricula/formulario";
    }

    @GetMapping("parceria")
    public String parceria(Model model) {
        model.addAttribute("empresaParceira", new EmpresaParceira());
        return "visitantes/parceria/formulario";
    }

    @PostMapping("/parceria/salvar")
    public String salvar(
            @Valid @ModelAttribute("empresaParceira") EmpresaParceira empresaParceira,
            BindingResult result, Model model, RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors()) {
            return "visitantes/parceria/formulario";
        }

        try {
            empresaParceira.setStatus("PENDENT");
            empresaParceiraService.salvar(empresaParceira);
            redirectAttributes.addFlashAttribute("sucesso", "Formulario enviado com sucesso!");
            return "redirect:/visitantes/parceria";
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("CPF")) {
                result.rejectValue("cpfRepresentante", "error.empresaParceira", e.getMessage());
            } else if (e.getMessage().contains("CNPJ")) {
                result.rejectValue("cnpj", "error.empresaParceira", e.getMessage());
            } else if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.empresaParceira", e.getMessage());
            }

            return "visitantes/parceira/formulario";
        } catch (Exception e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("empresaParceira", empresaParceira);
            return "visitantes/parceira/formulario";
        }
    }

    @GetMapping("login")
    public String login() {
        return "redirect:/login";
    }
}

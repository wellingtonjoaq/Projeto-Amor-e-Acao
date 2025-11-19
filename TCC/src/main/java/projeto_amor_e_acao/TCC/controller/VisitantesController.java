package projeto_amor_e_acao.TCC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("visitantes")
public class VisitantesController {

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
    public String voluntario() {
        return "visitantes/voluntario/formulario";
    }

    @GetMapping("matricula")
    public String matricula() {
        return "visitantes/matricula/formulario";
    }

    @GetMapping("parceria")
    public String parceria() {
        return "visitantes/parceria/formulario";
    }

    @GetMapping("login")
    public String login() {
        return "redirect:/login";
    }
}

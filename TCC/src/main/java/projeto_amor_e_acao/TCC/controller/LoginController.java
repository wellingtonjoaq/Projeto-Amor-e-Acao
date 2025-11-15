package projeto_amor_e_acao.TCC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/login/esqueciSenha")
    public String esqueciSenha() {
        return "login/esqueceuSenha"; // nova página que você vai criar
    }
}

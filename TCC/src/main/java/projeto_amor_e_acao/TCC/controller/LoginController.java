package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projeto_amor_e_acao.TCC.service.RedefinirSenhaTokenService;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private RedefinirSenhaTokenService redefinirSenhaTokenService;

    @GetMapping("")
    public String login() {
        return "login/login";
    }

    @GetMapping("/esqueciSenha")
    public String esqueciSenha() {
        return "login/esqueceuSenha";
    }

    @PostMapping("/processar")
    public String processar(@RequestParam String email, Model model) {

        redefinirSenhaTokenService.gerarTokenParaEmail(email);

        model.addAttribute("mensagem",
                "Se o e-mail existir em nossa base, você receberá um link para redefinir a senha.");

        return "login/esqueceuSenha";
    }

    @GetMapping("/redefinir")
    public String redefinir(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "login/redefinirSenha";
    }

    @PostMapping("/salvarNovaSenha")
    public String processarReset(
            @RequestParam String token,
            @RequestParam String senha,
            Model model) {

        boolean redefinido = redefinirSenhaTokenService.redefinirSenha(token, senha);

        if (!redefinido) {
            model.addAttribute("erro", "Token inválido ou expirado.");
            model.addAttribute("token", token);
            return "login/redefinirSenha";
        }

        model.addAttribute("mensagem", "Senha redefinida com sucesso! Faça login.");
        return "login/login";
    }
}

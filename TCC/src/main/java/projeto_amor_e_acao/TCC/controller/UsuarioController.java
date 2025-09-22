package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.service.UsuarioService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
            return "usuarios/visualizar";
        } else {
            return "redirect:/usuarios";
        }
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("acao", "criar");
        return "usuarios/formulario";
    }

    @PostMapping
    public String salvarUsuario(@Valid Usuario usuario,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "usuarios/formulario";
        }

        try {
            usuarioService.save(usuario);
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("senha")) {
                result.rejectValue("senha", "error.usuario", e.getMessage());
            } else {
                result.rejectValue("email", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "usuarios/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("acao", "editar");
        return "usuarios/formulario";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarUsuario(@PathVariable Long id,
                                   @Valid Usuario usuario,
                                   BindingResult result, Model model) {
        if (result.hasFieldErrors("nome") || result.hasFieldErrors("email")) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            return "usuarios/formulario";
        }

        try {
            usuarioService.atualizarUsuario(id, usuario);
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("senha")) {
                result.rejectValue("senha", "error.usuario", e.getMessage());
            } else {
                result.rejectValue("email", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            return "usuarios/formulario";
        } catch (Exception e) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            model.addAttribute("erro", e.getMessage());
            return "usuarios/formulario";
        }
    }

    @PostMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }
}

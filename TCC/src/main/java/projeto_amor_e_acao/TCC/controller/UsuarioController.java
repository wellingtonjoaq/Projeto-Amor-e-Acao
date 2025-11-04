package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.service.UsuarioService;
import java.util.Optional;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping()
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("acao", "criar");
        return "usuario/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Usuario usuario,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "usuario/formulario";
        }

        try {
            service.save(usuario);
            return "redirect:/usuario/listar";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("senha")) {
                result.rejectValue("senha", "error.usuario", e.getMessage());
            } else {
                result.rejectValue("email", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "usuario/formulario";
        }
    }

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 Model model) {

        Page<Usuario> usuarios = service.listarAtivos(page, size);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("paginaAtual", page);
        return "usuario/listar";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        Page<Usuario> usuarios = service.filtrarPesquisa(pesquisa, page, size);

        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("vazio", usuarios.isEmpty());

        return "usuario/pesquisaFiltro/lista";
    }


    @GetMapping("filtrar")
    public String filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cargo,
            Model model) {

        boolean temCargo = (cargo != null && !cargo.isBlank());

        Page<Usuario> usuarios = service.filtrar(cargo, page, size);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("vazio", false);
        model.addAttribute("cargo", cargo);

        if (!temCargo) {
            model.addAttribute("vazio", true);
        }

        if (usuarios.isEmpty()){
            model.addAttribute("vazio", usuarios.isEmpty());
        }

        return "usuario/filtro/lista";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = service.findById(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
            return "usuario/visualizar";
        } else {
            return "redirect:/usuario/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = service.findById(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuario/listar";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("acao", "editar");
        return "usuario/formulario";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id,
                                   @Valid Usuario usuario,
                                   BindingResult result, Model model)
    {
        if (result.hasFieldErrors("nome") || result.hasFieldErrors("email")) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            return "usuario/formulario";
        }

        try {
            service.atualizarUsuario(id, usuario);
            return "redirect:/usuario/listar";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("senha")) {
                result.rejectValue(
                        "senha", "error.usuario", e.getMessage());
            } else {
                result.rejectValue(
                        "email", "error.usuario", e.getMessage());
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            return "usuario/formulario";
        } catch (Exception e) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            model.addAttribute("erro", e.getMessage());
            return "usuario/formulario";
        }
    }

    @PostMapping("/remover/{id}")
    public String removerUsuario(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/usuario/listar";
    }


}

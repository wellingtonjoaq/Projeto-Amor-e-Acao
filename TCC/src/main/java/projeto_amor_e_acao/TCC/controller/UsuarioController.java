package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.service.FirebaseStorageService;
import projeto_amor_e_acao.TCC.service.UsuarioService;
import java.util.Optional;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private FirebaseStorageService firebaseService;

    @GetMapping()
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("acao", "criar");
        return "administrativo/usuario/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Usuario usuario,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "administrativo/usuario/formulario";
        }

        try {
            if (file != null && !file.isEmpty()) {
                String url = firebaseService.uploadFile(file);
                usuario.setFotoPerfil(url);
            }

            service.salvar(usuario);
            return "redirect:/usuario/listar";
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "administrativo/usuario/formulario";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Senha")) {
                result.rejectValue("senha", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "administrativo/usuario/formulario";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "administrativo/usuario/formulario";
        }
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid Usuario usuario,
                            BindingResult result,
                            @RequestParam(value = "file", required = false) MultipartFile file,
                            Model model) {
        if (result.hasFieldErrors("nome") || result.hasFieldErrors("email")) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            return "administrativo/usuario/formulario";
        }

        try {
            var usuarioExistente = service.buscaPorId(id);

            if (file != null && !file.isEmpty()) {
                String novaUrl = firebaseService.uploadFile(file);

                if (usuarioExistente.getFotoPerfil() != null && !usuarioExistente.getFotoPerfil().isBlank()) {
                    firebaseService.deleteFile(usuarioExistente.getFotoPerfil());
                }

                usuario.setFotoPerfil(novaUrl);
            } else {
                usuario.setFotoPerfil(usuarioExistente.getFotoPerfil());
            }

            service.atualizar(id, usuario);
            return "redirect:/usuario/listar";

        } catch (IllegalStateException e) {
            if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            return "administrativo/usuario/formulario";

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Senha")) {
                result.rejectValue("senha", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            return "administrativo/usuario/formulario";

        } catch (Exception e) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "editar");
            model.addAttribute("erro", e.getMessage());
            return "administrativo/usuario/formulario";
        }
    }

    @GetMapping("listar")
    public String listar(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 Model model) {

        Page<Usuario> usuarios = service.listarAtivos(page, size);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("paginaAtual", page);
        return "administrativo/usuario/lista";
    }

    @GetMapping("filtrarPesquisa")
    public String filtrarPesquisa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String pesquisa,
            Model model) {

        if (!pesquisa.isEmpty()){
            Page<Usuario> usuarios = service.filtrarPesquisa("ATIVO",pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", usuarios.isEmpty());

            return "administrativo/usuario/pesquisaFiltro/lista";
        }
        else {
            return "redirect:/usuario/listar";
        }

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
            return "redirect:/usuario/listar";
        }

        if (usuarios.isEmpty()){
            model.addAttribute("vazio", usuarios.isEmpty());
        }

        return "administrativo/usuario/filtro/lista";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = service.buscarPorId(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
            return "administrativo/usuario/visualizar";
        } else {
            return "redirect:/usuario/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = service.buscarPorId(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuario/listar";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("acao", "editar");
        return "administrativo/usuario/formulario";
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        service.deletarPorId(id);
        return "redirect:/usuario/listar";
    }
}

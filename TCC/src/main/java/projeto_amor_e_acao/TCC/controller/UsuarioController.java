package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projeto_amor_e_acao.TCC.dto.NotificacaoDTO;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.service.FirebaseStorageService;
import projeto_amor_e_acao.TCC.service.NotificacaoService;
import projeto_amor_e_acao.TCC.service.UsuarioService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private FirebaseStorageService firebaseService;

    @Autowired
    private NotificacaoService notificacaoService;

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado() {
        return service.getUsuarioLogado();
    }

    @ModelAttribute("notificacoesMenu")
    public List<NotificacaoDTO> carregarNotifMenu() {
        return notificacaoService.listarNotificacaoLimitado(7);
    }

    @GetMapping()
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("acao", "criar");
        return "administrativo/usuario/formulario";
    }

    @PostMapping("salvar")
    public String salvar(@Valid Usuario usuario,
                         BindingResult result,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("acao", "criar");
            return "administrativo/usuario/formulario";
        }

        try {
            Usuario salvo = service.salvar(usuario);

            if (file != null && !file.isEmpty()) {
                String url = firebaseService.uploadFile(file);
                salvo.setFotoPerfil(url);
                service.atualizar(salvo.getId(), salvo);
            }

            redirectAttributes.addFlashAttribute("sucesso", "Usuário salvo com sucesso!");
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
                            @Valid Usuario usuarioForm,
                            BindingResult result,
                            @RequestParam(value = "file", required = false) MultipartFile file,
                            Model model) {

        if (result.hasFieldErrors("nome") || result.hasFieldErrors("email")) {
            model.addAttribute("usuario", usuarioForm);
            model.addAttribute("acao", "editar");
            return "administrativo/usuario/formulario";
        }

        try {
            Usuario usuarioExistente = service.buscaPorId(id);

            usuarioExistente.setNome(usuarioForm.getNome());
            usuarioExistente.setEmail(usuarioForm.getEmail());
            usuarioExistente.setCargo(usuarioForm.getCargo());
            usuarioExistente.setSenha(usuarioForm.getSenha());

            if (file != null && !file.isEmpty()) {

                String novaUrl = firebaseService.uploadFile(file);

                if (usuarioExistente.getFotoPerfil() != null && !usuarioExistente.getFotoPerfil().isBlank()) {
                    firebaseService.deleteFile(usuarioExistente.getFotoPerfil());
                }

                usuarioExistente.setFotoPerfil(novaUrl);
            }

            service.atualizar(id, usuarioExistente);

            return "redirect:/usuario/listar";

        } catch (IllegalStateException e) {
            if (e.getMessage().contains("E-mail")) {
                result.rejectValue("email", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuarioForm);
            model.addAttribute("acao", "editar");
            return "administrativo/usuario/formulario";

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Senha")) {
                result.rejectValue("senha", "error.usuario", e.getMessage());
            }
            model.addAttribute("usuario", usuarioForm);
            model.addAttribute("acao", "editar");
            return "administrativo/usuario/formulario";

        } catch (Exception e) {
            model.addAttribute("usuario", usuarioForm);
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
            Page<Usuario> usuario = service.filtrarPesquisa("ATIVO",pesquisa, page, size);

            model.addAttribute("pesquisa", pesquisa);
            model.addAttribute("usuarios", usuario);
            model.addAttribute("paginaAtual", page);
            model.addAttribute("vazio", usuario.isEmpty());


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

        Page<Usuario> usuario = service.filtrar(cargo, page, size);

        model.addAttribute("usuarios", usuario);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("vazio", false);
        model.addAttribute("cargo", cargo);

        if (!temCargo) {
            return "redirect:/usuario/listar";
        }

        if (usuario.isEmpty()){
            model.addAttribute("vazio", usuario.isEmpty());
        }

        Usuario usuarios = service.getUsuarioLogado();
        model.addAttribute("usuarioLogado", usuarios);

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
        Usuario usuarios = service.getUsuarioLogado();
        model.addAttribute("usuarioLogado", usuarios);
        model.addAttribute("usuario", usuario);
        model.addAttribute("acao", "editar");
        return "administrativo/usuario/formulario";
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = service.getUsuarioLogado();

        try {
            if (usuarioLogado.getId().equals(id)) {
                throw new IllegalStateException("Você não pode deletar a si mesmo!");
            }
            service.deletarPorId(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário deletado com sucesso!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/usuario/listar";
    }


}

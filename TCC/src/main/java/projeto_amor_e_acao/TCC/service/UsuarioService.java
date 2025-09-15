package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setCargo(usuarioAtualizado.getCargo());
        usuarioExistente.setStatus(usuarioAtualizado.getStatus());
        usuarioExistente.setFotoPerfil(usuarioAtualizado.getFotoPerfil());

        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario save(Usuario usuario) {
        //Impedir e-mail duplicado
        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(usuario.getId())) {
                        throw new IllegalArgumentException("E-mail já está em uso");
                    }
                });

        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        //Não permitir excluir o último administrador ativo
        if (usuario.getCargo() == Usuario.Cargo.USUARIO_ADMINISTRADOR
                && usuario.getStatus() == Usuario.Status.ATIVO)
        {
            long adminsAtivos = usuarioRepository.findAll().stream().filter(
                    u -> u.getCargo() == Usuario.Cargo.USUARIO_ADMINISTRADOR
                    && u.getStatus() == Usuario.Status.ATIVO).count();

            if (adminsAtivos <= 1) {
                throw new IllegalStateException(
                        "Não é possível excluir o último administrador ativo");
            }
        }

        usuarioRepository.deleteById(id);
    }
}

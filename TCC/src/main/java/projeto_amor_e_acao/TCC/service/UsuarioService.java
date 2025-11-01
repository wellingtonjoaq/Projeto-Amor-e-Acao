package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Usuario> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public List<Usuario> listarTodosInativos() {
        return repository.findByStatusIgnoreCase("INATIVO");
    }

    public Page<Usuario> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Optional<Usuario> existingUserWithEmail = repository.findByEmail(usuarioAtualizado.getEmail());
        if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("E-mail já está em uso");
        }

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setCargo(usuarioAtualizado.getCargo());
        usuarioExistente.setStatus(usuarioAtualizado.getStatus());
        usuarioExistente.setFotoPerfil(usuarioAtualizado.getFotoPerfil());

        // Lógica de atualização da senha:
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            if (usuarioAtualizado.getSenha().length() < 6) {
                throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
            }
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        return repository.save(usuarioExistente);
    }

    public Usuario save(Usuario usuario) {
        // Validação para novos usuários
        if (usuario.getId() == null) {
            if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                throw new IllegalArgumentException("A senha é obrigatória para novos usuários.");
            }
            if (usuario.getSenha().length() < 6) {
                throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
            }
        }

        repository.findByEmail(usuario.getEmail())
                .ifPresent(existing -> {
                    if (usuario.getId() == null || !existing.getId().equals(usuario.getId())) {
                        throw new IllegalArgumentException("E-mail já está em uso");
                    }
                });

        // Criptografa a senha somente se ela existir e não estiver em branco.
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return repository.save(usuario);
    }

    public void deleteById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        //Não permitir excluir o último administrador ativo
        if (usuario.getCargo() == Usuario.Cargo.USUARIO_ADMINISTRADOR
                && usuario.getStatus().equals("INATIVO"))
        {
            long adminsAtivos = repository.findAll().stream().filter(
                    u -> u.getCargo() == Usuario.Cargo.USUARIO_ADMINISTRADOR
                            && u.getStatus().equals("INATIVO")).count();

            if (adminsAtivos <= 1) {
                throw new IllegalStateException(
                        "Não é possível excluir o último administrador ativo");
            }
        }

        repository.deleteById(id);
    }
}

package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.Curso;
import projeto_amor_e_acao.TCC.model.EmpresaParceira;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService{

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    public Usuario salvar(Usuario usuario) {
        usuario.setDataAlteracaoStatus(LocalDate.now());

        var existenteEmail = repository.findByEmailIgnoreCase(usuario.getEmail());
        if (existenteEmail.isPresent()) {
            throw new IllegalStateException("( Esse E-mail já existe! )");
        }

        if (usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("( Deve ter no mínimo 6 caracteres )");
        }
        if (!usuario.getSenha().matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("( Deve conter pelo menos uma letra maiúscula )");
        }
        if (!usuario.getSenha().matches(".*\\d.*")) {
            throw new IllegalArgumentException("( Deve conter pelo menos um número )");
        }
        if (!usuario.getSenha().matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~\\-].*")) {
            throw new IllegalArgumentException("( Deve conter pelo menos um caractere especial )");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        try {
            return repository.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar usuario.", e);
        }
    }

    public Usuario atualizar(Long id, Usuario usuario) {

        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("( Usuário não encontrado )"));

        var existenteEmail = repository.findByEmailIgnoreCase(usuario.getEmail());
        if (existenteEmail.isPresent() && !existenteEmail.get().getId().equals(id)) {
            throw new IllegalStateException("( Esse E-mail já existe! )");
        }

        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setCargo(usuario.getCargo());
        usuarioExistente.setStatus(usuario.getStatus());
        usuarioExistente.setFotoPerfil(usuario.getFotoPerfil());
        usuarioExistente.setDataAlteracaoStatus(LocalDate.now());

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {

            if (usuario.getSenha().length() < 6) {
                throw new IllegalArgumentException("( Deve ter no mínimo 6 caracteres )");
            }
            if (!usuario.getSenha().matches(".*[A-Z].*")) {
                throw new IllegalArgumentException("( Deve conter pelo menos uma letra maiúscula )");
            }
            if (!usuario.getSenha().matches(".*\\d.*")) {
                throw new IllegalArgumentException("( Deve conter pelo menos um número )");
            }
            if (!usuario.getSenha().matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~\\-].*")) {
                throw new IllegalArgumentException("( Deve conter pelo menos um caractere especial )");
            }
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        try {
            return repository.save(usuarioExistente);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao atualizar usuário.", e);
        }
    }


    public Page<Usuario> listarAtivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }

    public Page<Usuario> listarInativos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatusIgnoreCase("INATIVO", pageable);
    }

    public Page<Usuario> filtrarPesquisa(String status, String pesquisa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (pesquisa == null || pesquisa.isBlank()) {
            return Page.empty(pageable);
        }

        pesquisa = pesquisa.trim();
        Page<Usuario> resultados = repository.findByStatusIgnoreCaseAndNomeContainingIgnoreCase(status, pesquisa, pageable);

        if (resultados.isEmpty()) {
            resultados = repository.findByStatusIgnoreCaseAndEmailContainingIgnoreCase(status, pesquisa, pageable);
        }

        return resultados;
    }


    public Page<Usuario> filtrar(String cargo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        boolean temCargo = (cargo != null && !cargo.isBlank() && !cargo.equalsIgnoreCase("TODOS"));

        if (temCargo) {
            Usuario.Cargo cargoEnum;

            try {
                cargoEnum = Usuario.Cargo.valueOf(cargo.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Page.empty();
            }

            return repository.findByStatusIgnoreCaseAndCargo(
                    "ATIVO",
                    cargoEnum,
                    pageable
            );
        }

        return repository.findByStatusIgnoreCase("ATIVO", pageable);
    }


    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Usuario buscaPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void deletarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (usuario.getCargo() == Usuario.Cargo.USUARIO_ADMINISTRADOR
                && usuario.getStatus().equals("ATIVO")) {
            long adminsAtivos = repository.findAll().stream()
                    .filter(u -> u.getCargo() == Usuario.Cargo.USUARIO_ADMINISTRADOR
                            && u.getStatus().equals("ATIVO"))
                    .count();

            if (adminsAtivos <= 1) {
                throw new IllegalStateException("Não é possível excluir o último administrador ativo");
            }
        }

        firebaseStorageService.deleteFile(usuario.getFotoPerfil());

        repository.deleteById(id);
    }


    public Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.isBlank()) return;

        if (senha.length() < 6) {
            throw new IllegalArgumentException("( Senha deve ter no mínimo 6 caracteres )");
        }
        if (!senha.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("( A senha deve conter pelo menos uma letra maiúscula )");
        }
        if (!senha.matches(".*\\d.*")) {
            throw new IllegalArgumentException("( A senha deve conter pelo menos um número )");
        }
        if (!senha.matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~\\-].*")) {
            throw new IllegalArgumentException("( A senha deve conter pelo menos um caractere especial )");
        }
    }
}

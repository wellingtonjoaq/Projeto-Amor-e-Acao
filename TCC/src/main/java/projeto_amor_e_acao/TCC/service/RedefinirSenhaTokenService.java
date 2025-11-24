package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.RedefinirSenhaToken;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.repository.RedefinirSenhaTokenRepository;
import projeto_amor_e_acao.TCC.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RedefinirSenhaTokenService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RedefinirSenhaTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void gerarTokenParaEmail(String email) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) return;

        Usuario usuario = usuarioOpt.get();
        String token = UUID.randomUUID().toString();

        Optional<RedefinirSenhaToken> tokenExistente = tokenRepository.findByUsuario(usuario);

        RedefinirSenhaToken resetToken;

        if (tokenExistente.isPresent()) {
            resetToken = tokenExistente.get();
        } else {
            resetToken = new RedefinirSenhaToken();
            resetToken.setUsuario(usuario);
        }

        resetToken.setToken(token);
        resetToken.setExpiracao(LocalDateTime.now().plusHours(1));

        tokenRepository.save(resetToken);

        String link = "http://localhost:8080/login/redefinir?token=" + token;

        String mensagem = """
            Você solicitou a redefinição da sua senha.

            Clique no link abaixo para criar uma nova senha:
            %s

            Caso você não tenha solicitado, ignore este e-mail.
            """.formatted(link);

        emailService.enviarEmail(email, "Redefinição de Senha", mensagem);
    }


    public boolean redefinirSenha(String token, String novaSenha) {

        RedefinirSenhaToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null) {
            return false;
        }

        if (resetToken.getExpiracao().isBefore(LocalDateTime.now())) {
            return false;
        }

        Usuario usuario = resetToken.getUsuario();

        if (novaSenha.length() < 6) {
            throw new IllegalArgumentException("( Deve ter no mínimo 6 caracteres )");
        }
        if (!novaSenha.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("( Deve conter pelo menos uma letra maiúscula )");
        }
        if (!novaSenha.matches(".*\\d.*")) {
            throw new IllegalArgumentException("( Deve conter pelo menos um número )");
        }
        if (!novaSenha.matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~\\-].*")) {
            throw new IllegalArgumentException("( Deve conter pelo menos um caractere especial )");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);

        return true;
    }

}

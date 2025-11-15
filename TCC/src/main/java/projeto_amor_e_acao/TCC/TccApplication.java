package projeto_amor_e_acao.TCC;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.repository.UsuarioRepository;
import projeto_amor_e_acao.TCC.service.UsuarioService;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class TccApplication {

	public static void main(String[] args) {
		SpringApplication.run(TccApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDataBase(UsuarioRepository usuarioRepository) {
		return args -> {
			String emailAdmin = "administrador@gmail.com";
			String emailUsuario = "usuariosimples@gmail.com";

			if (usuarioRepository.findByEmail(emailAdmin).isEmpty()) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				Usuario admin = new Usuario(null, "admin", emailAdmin, encoder.encode("12345678"), Usuario.Cargo.USUARIO_ADMINISTRADOR, "ATIVO", LocalDate.now(), null);
				usuarioRepository.save(admin);
				System.out.println("Usuário administrador criado com sucesso!");
			} else {
				System.out.println("Usuário administrador já existe. Nenhuma ação necessária.");
			}

			if (usuarioRepository.findByEmail(emailUsuario).isEmpty()) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				Usuario simples = new Usuario(null, "admin", emailUsuario, encoder.encode("12345678"), Usuario.Cargo.USUARIO_SIMPLES, "ATIVO", LocalDate.now(), null);
				usuarioRepository.save(simples);
				System.out.println("Usuário simples criado com sucesso!");
			} else {
				System.out.println("Usuário simples já existe. Nenhuma ação necessária.");
			}
		};
	}
}

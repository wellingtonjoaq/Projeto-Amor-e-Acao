package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Size(min = 3, max = 100, message = "( Deve conter entre 3 a 100 caracteres )")
    private String nome;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Email(message = "( Campo Invalido )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "( Campo Obrigatório )")
    private Cargo cargo;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "( Campo Obrigatorio )")
    private String status = "ATIVO";

    @Column(name = "data_alteracao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAlteracaoStatus;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    public enum Cargo {
        USUARIO_SIMPLES,
        USUARIO_ADMINISTRADOR
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.cargo == Cargo.USUARIO_ADMINISTRADOR) {
            return Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status.equals("ATIVO");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals("ATIVO"); // O usuário está habilitado se estiver ativo
    }
}

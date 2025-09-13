package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuario")
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
    @jakarta.validation.constraints.NotBlank(message = "O nome é obrigatório")
    @jakarta.validation.constraints.Size(
            min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Column(unique = true, nullable = false)
    @jakarta.validation.constraints.NotBlank(message = "O e-mail é obrigatório")
    @jakarta.validation.constraints.Email(message = "E-mail inválido")
    private String email;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotBlank(message = "A senha é obrigatória")
    @jakarta.validation.constraints.Size(
            min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "O cargo é obrigatório")
    private Cargo cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "O status é obrigatório")
    private Status status;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    public enum Cargo {
        USUARIO_SIMPLES,
        USUARIO_ADMINISTRADOR
    }

    public enum Status {
        ATIVO,
        INATIVO
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
        return this.status == Status.ATIVO;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == Status.ATIVO; // O usuário está habilitado se estiver ativo
    }
}

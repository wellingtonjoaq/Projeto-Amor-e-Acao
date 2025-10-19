package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 3, max = 100, message = "( Deve conter entre 3 a 100 caracteres )")
    private String nome;

    @Column(nullable = false, length = 14, unique = true)
    @NotBlank(message = "( Campo Obrigatório )")
    @CPF(message = "( Campo Invalido )")
    private String cpf;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "( Campo Obrigatório )")
    @Email(message = "( Campo Invalido )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String email;

    @Column(nullable = false, length = 11)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 11, max = 15, message = "( Campo Invalido )")
    private String telefone;

    @Column(nullable = false, length = 9)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 5, max = 9, message = "( Campo Invalido )")
    private String genero;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatório )")
    private String funcao;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String motivacao;

    @Column(nullable = false, length = 8)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 8, max = 8, message = "( Campo Invalido )")
    private String cep;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String bairro;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String endereco;

    @Column(nullable = false, name = "nr_casa", length = 4)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 4, max = 4, message = "( Campo Invalido )")
    private String nrCasa;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String cidade;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String estado;

    @Column(nullable = false, length = 7)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 5, max = 7, message = "( Campo Invalido )")
    private String status = "ATIVO";

    @Column(name = "data_alteracao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAlteracaoStatus;

    @PrePersist
    @PreUpdate
    private void normalizar() {
        if (cpf != null) {
            cpf = cpf.replaceAll("\\D", "");
        }
        if (email != null) {
            email = email.trim().toLowerCase();
        }
        if (cep != null){
            cep = cep.replaceAll("\\D", "");
        }
        if (telefone != null){
            telefone = telefone.replaceAll("\\D", "");
        }
    }
}

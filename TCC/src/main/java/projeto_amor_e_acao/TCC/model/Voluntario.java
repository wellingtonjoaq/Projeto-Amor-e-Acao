package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

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

    @Column
    private String nome;

    @Column(unique = true)
    @NotBlank(message = "O campo cpf é obrigatório")
    @CPF(message = "CPF Invalido")
    private String cpf;

    @Column(unique = true)
    @Email(message = "Email invalido")
    private String email;

    @Column
    private String telefone;

    @Column
    private String funcao;

    @Column
    private String motivacao;

    @Column
    private String cep;

    @Column
    private String bairro;

    @Column
    private String endereco;

    @Column(name = "nr_casa")
    private String nrCasa;

    @Column
    private String cidade;

    @Column
    private String estado;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "O campo status é obrigatório")
    private String status = "ATIVO";

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (cpf != null) {
            cpf = cpf.replaceAll("\\D", "");
        }
        if (email != null) {
            email = email.trim().toLowerCase();
        }
    }
}

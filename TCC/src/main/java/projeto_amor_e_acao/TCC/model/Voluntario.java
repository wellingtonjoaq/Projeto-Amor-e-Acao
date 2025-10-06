package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF Invalido")
    private String cpf;

    @Column(unique = true)
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email invalido")
    private String email;

    @Column(length = 11)
    @NotBlank(message = "Telefone é obrigatorio")
    @Size(min = 11, max = 15, message = "Telefone Invalido")
    private String telefone;

    @Column(length = 9)
    @NotBlank(message = "Genero é obrigatorio")
    private String genero;

    @Column
    @NotBlank(message = "Função é obrigatorio")
    private String funcao;

    @Column
    private String motivacao;

    @Column(length = 8)
    private String cep;

    @Column
    private String bairro;

    @Column
    private String endereco;

    @Column(name = "nr_casa", length = 4)
    private String nrCasa;

    @Column
    @NotBlank(message = "Cidade é obrigatorio")
    private String cidade;

    @Column
    @NotBlank(message = "Estado é obrigatorio")
    private String estado;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Status é obrigatorio")
    private String status = "ATIVO";

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

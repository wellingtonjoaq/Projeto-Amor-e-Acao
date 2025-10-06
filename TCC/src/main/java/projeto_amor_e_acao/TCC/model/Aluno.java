package projeto_amor_e_acao.TCC.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Parent;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O campo nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter no minimo 3 e no maximo 100 caracteres")
    private String nome;

    @Column(nullable = false, length = 14, unique = true)
    @NotBlank(message = "O campo cpf é obrigatório")
    @CPF(message = "CPF Invalido")
    private String cpf;

    @Column(unique = true)
    @NotBlank(message = "Email é obrigatorio")
    @Email(message = "Email Invalido")
    @Size(max = 255)
    private String email;

    @Column(length = 11)
    @NotBlank(message = "Telefone é obrigatorio")
    @Size(min = 11, max = 15, message = "Telefone Invalido")
    private String telefone;

    @Column(length = 9)
    @NotBlank(message = "Genero é obrigatorio")
    private String genero;

    @Column(length = 8)
    private String cep;

    @Column
    private String bairro;

    @Column
    private String endereco;

    @Column(name = "nr_casa", length = 4)
    private String nrCasa;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "O campo status é obrigatório")
    private String status = "ATIVO";

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Matricula> matriculas = new ArrayList<>();

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

package projeto_amor_e_acao.TCC.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 3, max = 100, message = "( Deve conter entre 3 a 100 caracteres )")
    private String nome;

    @Column(nullable = false, length = 14, unique = true)
    @NotBlank(message = "( Campo Obrigatório )")
    @CPF(message = "( Campo Invalido )")
    private String cpf;

    @Column(unique = true)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Email(message = "( Campo Invalido )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String email;

    @Column(length = 11)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Size(min = 11, max = 15, message = "( Campo Invalido )")
    private String telefone;

    @Column(length = 9)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Size(min = 8, max = 9, message = "( Campo Invalido )")
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
    @NotBlank(message = "( Campo Obrigatorio )")
    private String status = "ATIVO";

    @Column(name = "data_alteracao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAlteracaoStatus;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
//    @NotEmpty(message = "( Escolha pelo menos um curso )")
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

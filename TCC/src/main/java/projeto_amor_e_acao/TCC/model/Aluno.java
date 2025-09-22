package projeto_amor_e_acao.TCC.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private String nome;

    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(unique = true)
    @Email
    private String email;

    @Column
    private String telefone;

    @Column
    private String genero;

    @Column
    private String cep;

    @Column
    private String bairro;

    @Column
    private String endereco;

    @Column(name = "nr_casa")
    private String nrCasa;

    @Column(nullable = false, length = 20)
    private String status;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Matricula> matriculas;


}

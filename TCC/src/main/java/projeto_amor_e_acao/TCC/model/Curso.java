package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column
    private String professor;

    @Column
    private String formacao;

    @Column
    private String cep;

    @Column
    private String bairro;

    @Column
    private String endereco;

    @Column
    private String nrLocal;

    @Column
    private int cargaHoraria;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    @Column
    private String foto;

    @Column
    private String descricao;

    @Column(nullable = false, length = 20)
    private String status;

    /*
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Matricula> matriculas;
    */
}

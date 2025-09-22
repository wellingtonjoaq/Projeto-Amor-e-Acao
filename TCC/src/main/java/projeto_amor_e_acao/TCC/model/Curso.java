package projeto_amor_e_acao.TCC.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "nr_local")
    private String nrLocal;

    @Column(name = "carga_horaria")
    private int cargaHoraria;

    @Column(name = "data_inicio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    @Column
    private String foto;

    @Column
    private String descricao;

    @Column(nullable = false, length = 20)
    private String status;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Matricula> matriculas;
}

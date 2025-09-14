package projeto_amor_e_acao.TCC.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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

    private String professor;
    private String formacao;
    private String cep;
    private String bairro;
    private String endereco;
    private String nrLocal;
    private int cargaHoraria;
    private Date dataComeco;
    private Date dataFim;
    private String foto;
    private String descricao;

    @Column(nullable = false, length = 20)
    private String status;

    /*
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Matricula> matriculas;
    */
}

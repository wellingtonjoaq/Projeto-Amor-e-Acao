package projeto_amor_e_acao.TCC.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import projeto_amor_e_acao.TCC.validation.DataInicioAntesDeDataFim;

import java.time.LocalDate;
import java.util.List;


@DataInicioAntesDeDataFim
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
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 3, max = 100, message = "( Deve conter entre 3 a 100 caracteres )")
    private String nome;

    @Column
    @NotBlank(message = "( Campo Obrigatório )")
    private String professor;

    @Column
    @NotBlank(message = "( Campo Obrigatório )")
    private String formacao;

    @Column(length = 8)
    private String cep;

    @Column
    private String bairro;

    @Column
    private String endereco;

    @Column(name = "nr_local", length = 4)
    private String nrLocal;

    @Column(name = "carga_horaria")
    @NotBlank(message = "( Campo Obrigatório )")
    private String cargaHoraria;

    @Column(name = "data_inicio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    @Column(length = 500)
    private String foto;

    @Column
    private String descricao;

    @Column(nullable = false)
    private String categorias;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "( Campo Obrigatório )")
    private String status = "ATIVO";

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Matricula> matriculas;

    @PrePersist
    @PreUpdate
    private void normalizar(){
        if (cep != null){
            cep = cep.replaceAll("\\D", "");
        }
    }
}

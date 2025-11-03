package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class RelatorioEvasaoDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCurso;
    private Long totalMatriculados;
    private Long totalEvasivos;
    private Double taxaEvasao;
}

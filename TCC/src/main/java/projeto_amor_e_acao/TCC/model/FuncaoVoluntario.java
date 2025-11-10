package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FuncaoVoluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 3, max = 100, message = "( Deve conter entre 3 a 100 caracteres )")
    private String nome;

    @Column(nullable = false)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(max = 500, message = "( Limite Excedido! )")
    private String descricao;
}

package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String funcao;
    private String genero;
    private String motivacao;
    private String cep;
    private String bairro;
    private String endereco;
    private String nrCasa;
    private String cidade;
    private String estado;
    private String status;
}

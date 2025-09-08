package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

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
    private String status;
}

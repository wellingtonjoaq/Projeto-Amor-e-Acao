package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.LongFunction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmpresaParceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nome_empresa;

    @Column(nullable = false)
    private Long cnpj;

    private String endereco;
    private String nome_representante;
    private Long cpf_representante;
    private String email;

    @Column(nullable = false)
    private Date data_inicio; //sql.Date

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_fim;

    private Long telefone;
    private String objetivo;
    private String termos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status{
        ATIVO,
        INATIVO
    }
}

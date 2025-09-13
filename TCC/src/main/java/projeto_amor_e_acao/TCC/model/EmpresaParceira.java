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

    @Column(nullable = false, length = 150)
    @jakarta.validation.constraints.NotBlank(
            message = "O nome da empresa é obrigatório")
    @jakarta.validation.constraints.Size(
            min = 3, max = 150,
            message = "O nome da empresa deve ter entre 3 e 150 caracteres")
    private String nome_empresa;

    @Column(nullable = false, unique = true)
    @jakarta.validation.constraints.NotNull(message = "O CNPJ é obrigatório")
    @jakarta.validation.constraints.Digits(
            integer = 14, fraction = 0,
            message = "O CNPJ deve conter 14 dígitos numéricos")
    private Long cnpj;

    @jakarta.validation.constraints.Size(
            max = 255, message = "O endereço pode ter no máximo 255 caracteres")
    private String endereco;

    @jakarta.validation.constraints.Size(
            max = 100,
            message = "O nome do representante pode ter no máximo 100 caracteres")
    private String nome_representante;

    @jakarta.validation.constraints.Digits(
            integer = 11, fraction = 0,
            message = "O CPF deve conter 11 dígitos numéricos")
    private Long cpf_representante;

    @jakarta.validation.constraints.Email(message = "E-mail inválido")
    private String email;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(
            message = "A data de início é obrigatória")
    private Date data_inicio; //sql.Date

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_fim;

    @jakarta.validation.constraints.Digits(
            integer = 11, fraction = 0,
            message = "O telefone deve conter até 11 dígitos")
    private Long telefone;

    @jakarta.validation.constraints.Size(
            max = 500, message = "O objetivo pode ter no máximo 500 caracteres")
    private String objetivo;

    private String termos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "O status é obrigatório")
    private Status status;

    public enum Status{
        ATIVO,
        INATIVO
    }
}

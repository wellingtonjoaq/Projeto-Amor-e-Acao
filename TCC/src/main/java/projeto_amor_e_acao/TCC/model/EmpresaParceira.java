package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Pattern;

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
    private String nome;

    @Column(nullable = false, unique = true)
    @jakarta.validation.constraints.NotNull(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "^\\d{14}$",
            message = "O CNPJ deve conter exatamente 14 dígitos numéricos")
    private String cnpj;

    @jakarta.validation.constraints.Size(
            max = 255, message = "O endereço pode ter no máximo 255 caracteres")
    private String endereco;

    @Column(nullable = false, length = 150)
    @jakarta.validation.constraints.NotBlank(
            message = "O nome do representante é obrigatório")
    @jakarta.validation.constraints.Size(
            max = 100,
            message = "O nome do representante pode ter no máximo 100 caracteres")
    private String nome_representante;

    @Pattern(regexp = "^\\d{11}$",
            message = "O CPF deve conter exatamente 11 dígitos numéricos")
    private String cpf_representante;

    @jakarta.validation.constraints.Email(message = "E-mail inválido")
    @jakarta.validation.constraints.NotNull(message = "O e-mail é obrigatório")
    private String email;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(
            message = "A data de início é obrigatória")
    private Date data_inicio; //sql.Date

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_fim;

    @Pattern(regexp = "^\\d{10,11}$",
            message = "O telefone deve conter 10 ou 11 dígitos numéricos")
    @jakarta.validation.constraints.NotNull(message = "O telefone é obrigatório")
    private String telefone;

    @jakarta.validation.constraints.Size(
            max = 500, message = "O objetivo pode ter no máximo 500 caracteres")
    private String objetivo;

    private String termos;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "( Campo Obrigatorio )")
    private String status = "ATIVO";

    @Column(name = "data_alteracao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAlteracaoStatus;
}

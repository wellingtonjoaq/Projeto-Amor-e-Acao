package projeto_amor_e_acao.TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import projeto_amor_e_acao.TCC.validation.DataInicioAntesDeDataFim;

import java.time.LocalDate;

@DataInicioAntesDeDataFim
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

    @Column(nullable = false, length = 100)
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 3, max = 100, message = "( Deve conter entre 3 a 100 caracteres )")
    private String nome;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Size(min = 14, max = 18, message = "( Campo Invalido )")
    private String cnpj;

    @Size(max = 255, message = "( Tamanho Excedido )")
    private String endereco;

    @Column(nullable = false, length = 100, name = "nome_representante")
    @NotBlank(message = "( Campo Obrigatório )")
    @Size(min = 3, max = 100, message = "( Deve conter entre 3 a 100 caracteres )")
    private String nomeRepresentante;

    @Column(nullable = false, length = 14, unique = true, name = "cpf_representante")
    @NotBlank(message = "( Campo Obrigatório )")
    @CPF(message = "( Campo Invalido )")
    private String cpfRepresentante;

    @Column(unique = true)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Email(message = "( Campo Invalido )")
    @Size(max = 255, message = "( Tamanho Excedido )")
    private String email;

    @Column(name = "data_inicio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "( Campo Obrigatório )")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    @Column(length = 11)
    @NotBlank(message = "( Campo Obrigatorio )")
    @Size(min = 11, max = 15, message = "( Campo Invalido )")
    private String telefone;

    @Size(max = 500, message = "( Tamanho Excedido )")
    private String objetivo;

    private String termos;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "( Campo Obrigatorio )")
    private String status = "ATIVO";

    @Column(name = "data_alteracao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAlteracaoStatus;

    @PrePersist
    @PreUpdate
    private void normalizar() {
        if (cpfRepresentante != null) {
            cpfRepresentante = cpfRepresentante.replaceAll("\\D", "");
        }
        if (cnpj != null) {
            cnpj = cnpj.replaceAll("\\D", "");
        }
        if (email != null) {
            email = email.trim().toLowerCase();
        }
        if (telefone != null){
            telefone = telefone.replaceAll("\\D", "");
        }
    }
}

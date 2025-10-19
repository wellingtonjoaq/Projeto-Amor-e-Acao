package projeto_amor_e_acao.TCC.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HistoricoDTO {
    private Long id;
    private String nome;
    private String email;
    private String status;
    private LocalDate dataAlteracaoStatus;

    public HistoricoDTO(Long id, String nome, String email, String status, LocalDate dataAlteracaoStatus) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.status = status;
        this.dataAlteracaoStatus = dataAlteracaoStatus;
    }
}

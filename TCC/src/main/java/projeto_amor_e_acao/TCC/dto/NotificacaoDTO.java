package projeto_amor_e_acao.TCC.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NotificacaoDTO {
    private Long id;
    private String nome;
    private LocalDate dataAlteracaoStatus;
    private String tipo;

    public NotificacaoDTO(Long id, String nome,LocalDate dataAlteracaoStatus, String tipo) {
        this.id = id;
        this.nome = nome;
        this.dataAlteracaoStatus = dataAlteracaoStatus;
        this.tipo = tipo;
    }
}

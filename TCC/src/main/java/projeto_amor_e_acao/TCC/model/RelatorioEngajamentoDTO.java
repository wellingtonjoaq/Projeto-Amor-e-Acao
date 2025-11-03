package projeto_amor_e_acao.TCC.model;

import lombok.Data;

import java.util.Map;

@Data
public class RelatorioEngajamentoDTO {

    private double taxaConversaoVisitante;
    private long totalAlunos;
    private long totalVoluntarios;
    private long totalRegistros;
    private Map<String, Long> usoFuncionalidades;
    private Long totalAcoesChave;
}

package projeto_amor_e_acao.TCC.model;

import lombok.Data;
import java.util.Map;

@Data
public class RelatorioVoluntarioDTO {

    private Map<String, Long> voluntariosAtivosPorFuncao;
    private Double tempoMedioPermanenciaDias;
    private Map<String, Long> distribuicaoPorGenero;
    private Map<String, Long> distribuicaoPorMotivacao;
}


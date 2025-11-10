package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.RelatorioVoluntarioDTO;
import projeto_amor_e_acao.TCC.repository.VoluntarioRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioVoluntarioService {

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    public RelatorioVoluntarioDTO gerarRelatorioVoluntariado() {
        RelatorioVoluntarioDTO relatorio = new RelatorioVoluntarioDTO();

        relatorio.setVoluntariosAtivosPorFuncao(
                convertObjectListToMap(voluntarioRepository.countActiveVolunteersByFunction())
        );

        relatorio.setDistribuicaoPorGenero(
                convertObjectListToMap(voluntarioRepository.countByGender())
        );

        relatorio.setDistribuicaoPorMotivacao(
                convertObjectListToMap(voluntarioRepository.countByMotivation())
        );

        return relatorio;
    }

    private Map<String, Long> convertObjectListToMap(List<Object[]> list) {
        return list.stream()
                .collect(Collectors.toMap(
                        arr -> {
                            if (arr[0] instanceof String) {
                                return (String) arr[0];
                            } else if (arr[0] != null) {
                                return arr[0].toString();
                            }
                            return "N/A";
                        },
                        arr -> (Long) arr[1]
                ));
    }
}

package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.RelatorioVoluntarioDTO;
import projeto_amor_e_acao.TCC.repository.VoluntarioRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RelatorioVoluntarioService {

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    public RelatorioVoluntarioDTO gerarRelatorioVoluntariado() {
        RelatorioVoluntarioDTO relatorio = new RelatorioVoluntarioDTO();

        relatorio.setVoluntariosAtivosPorFuncao(
                convertObjectListToMap(voluntarioRepository.countActiveVolunteersByFunction())
        );

        relatorio.setTempoMedioPermanenciaDias(calcularTempoMedioPermanencia());

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

    private Double calcularTempoMedioPermanencia() {
        List<Object[]> inativos = voluntarioRepository.findInactiveVolunteersStatusChangeDate();
        List<Object[]> ativos = voluntarioRepository.findActiveVolunteersStatusChangeDate();

        Stream<Long> duracaoInativos = inativos.stream()
                .map(arr -> 1L /* ajustar segundo lógica desejada */);

        Stream<Long> duracaoAtivos = ativos.stream()
                .map(arr -> {
                    LocalDate dataInicio = (LocalDate) arr[1];
                    if (dataInicio == null) {
                        dataInicio = LocalDate.now();
                    }
                    return ChronoUnit.DAYS.between(dataInicio, LocalDate.now());
                });

        List<Long> todasDuracoes = Stream.concat(duracaoInativos, duracaoAtivos)
                .filter(d -> d >= 0)
                .collect(Collectors.toList());

        if (todasDuracoes.isEmpty()) {
            return 0.0;
        }

        double somaDuracoes = todasDuracoes.stream().mapToDouble(Long::doubleValue).sum();
        return somaDuracoes / todasDuracoes.size();
    }
}

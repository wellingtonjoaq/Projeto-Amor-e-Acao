package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.dto.RelatorioVoluntarioDTO;
import projeto_amor_e_acao.TCC.repository.VoluntarioRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public RelatorioVoluntarioDTO gerarRelatorioVoluntariadoFiltrado(LocalDateTime dataInicio, LocalDateTime dataFim) {
        RelatorioVoluntarioDTO relatorio = new RelatorioVoluntarioDTO();
        LocalDate dataInicioLocalDate = dataInicio != null ? dataInicio.toLocalDate() : null;
        LocalDate dataFimLocalDate = dataFim != null ? dataFim.toLocalDate() : null;

        List<Object[]> funcaoCounts;
        List<Object[]> generoCounts;
        List<Object[]> motivacaoCounts;

        if (dataInicioLocalDate != null && dataFimLocalDate != null) {
            funcaoCounts = voluntarioRepository.countActiveVolunteersByFunctionBetweenDates(dataInicioLocalDate, dataFimLocalDate);
            generoCounts = voluntarioRepository.countByGenderBetweenDates(dataInicioLocalDate, dataFimLocalDate);
            motivacaoCounts = voluntarioRepository.countByMotivationBetweenDates(dataInicioLocalDate, dataFimLocalDate);
        } else {
            funcaoCounts = voluntarioRepository.countActiveVolunteersByFunction();
            generoCounts = voluntarioRepository.countByGender();
            motivacaoCounts = voluntarioRepository.countByMotivation();
        }

        relatorio.setVoluntariosAtivosPorFuncao(convertObjectListToMap(funcaoCounts));
        relatorio.setDistribuicaoPorGenero(convertObjectListToMap(generoCounts));
        relatorio.setDistribuicaoPorMotivacao(convertObjectListToMap(motivacaoCounts));

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
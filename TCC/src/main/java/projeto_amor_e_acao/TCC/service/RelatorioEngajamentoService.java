package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.dto.RelatorioEngajamentoDTO;
import projeto_amor_e_acao.TCC.repository.AlunoRepository;
import projeto_amor_e_acao.TCC.repository.LogAuditoriaRepository;
import projeto_amor_e_acao.TCC.repository.VoluntarioRepository;

import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RelatorioEngajamentoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private LogAuditoriaRepository logAuditoriaRepository;

    public RelatorioEngajamentoDTO gerarRelatorioEngajamento() {
        RelatorioEngajamentoDTO dto = new RelatorioEngajamentoDTO();

        long totalAlunos = alunoRepository.count();
        long totalVoluntarios = voluntarioRepository.count();
        long totalRegistros = totalAlunos + totalVoluntarios;

        long totalAcoesChave = logAuditoriaRepository.countTotalAcoesChave();
        double taxaConversao = totalRegistros == 0
                ? 0.0 : ((double) totalAcoesChave / totalRegistros) * 100;

        Map<String, Long> usoFuncionalidades =
                logAuditoriaRepository.countAcoesAgrupadasPorUsuarioEAcao()
                        .stream().collect(Collectors.toMap(
                                arr -> (String) arr[0], arr -> (Long) arr[1]));

        dto.setTotalAlunos(totalAlunos);
        dto.setTotalVoluntarios(totalVoluntarios);
        dto.setTotalRegistros(totalRegistros);
        dto.setTotalAcoesChave(totalAcoesChave);
        dto.setTaxaConversaoVisitante(taxaConversao);
        dto.setUsoFuncionalidades(usoFuncionalidades);

        return dto;
    }

    public RelatorioEngajamentoDTO gerarRelatorioEngajamentoFiltrado(LocalDateTime dataInicio, LocalDateTime dataFim) {
        RelatorioEngajamentoDTO dto = new RelatorioEngajamentoDTO();

        long totalAlunos = alunoRepository.count();
        long totalVoluntarios = voluntarioRepository.count();
        long totalRegistros = totalAlunos + totalVoluntarios;

        long totalAcoesChave;
        Map<String, Long> usoFuncionalidades;

        if (dataInicio != null && dataFim != null) {
            totalAcoesChave = logAuditoriaRepository.countTotalAcoesChaveBetweenDates(dataInicio, dataFim);

            usoFuncionalidades = logAuditoriaRepository.countAcoesAgrupadasPorUsuarioEAcaoBetweenDates(dataInicio, dataFim)
                    .stream().collect(Collectors.toMap(arr -> (String) arr[0], arr -> (Long) arr[1]));
        } else {
            totalAcoesChave = logAuditoriaRepository.countTotalAcoesChave();

            usoFuncionalidades = logAuditoriaRepository.countAcoesAgrupadasPorUsuarioEAcao()
                    .stream().collect(Collectors.toMap(arr -> (String) arr[0], arr -> (Long) arr[1]));
        }

        double taxaConversao = totalRegistros == 0 ? 0.0 : ((double) totalAcoesChave / totalRegistros) * 100;

        dto.setTotalAlunos(totalAlunos);
        dto.setTotalVoluntarios(totalVoluntarios);
        dto.setTotalRegistros(totalRegistros);
        dto.setTotalAcoesChave(totalAcoesChave);
        dto.setTaxaConversaoVisitante(taxaConversao);
        dto.setUsoFuncionalidades(usoFuncionalidades);

        return dto;
    }
}
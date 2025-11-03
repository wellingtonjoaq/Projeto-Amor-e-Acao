package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.LogAuditoria;
import projeto_amor_e_acao.TCC.repository.LogAuditoriaRepository;

import java.time.LocalDateTime;

@Service
public class LogAuditoriaService {

    @Autowired
    private LogAuditoriaRepository logsAuditoriaRepository;

    public void registrarLog(Long usuarioId, String acao) {
        LogAuditoria log = new LogAuditoria();
        log.setUsuarioId(usuarioId);
        log.setAcao(acao);
        log.setDataHora(LocalDateTime.now());

        logsAuditoriaRepository.save(log);
    }
}

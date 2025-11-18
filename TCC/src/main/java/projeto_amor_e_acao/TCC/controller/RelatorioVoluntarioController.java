package projeto_amor_e_acao.TCC.controller;

import org.openpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projeto_amor_e_acao.TCC.dto.RelatorioVoluntarioDTO;
import projeto_amor_e_acao.TCC.service.PdfExportVoluntarioService;
import projeto_amor_e_acao.TCC.service.RelatorioVoluntarioService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/relatorios/voluntariado")
public class RelatorioVoluntarioController {

    @Autowired
    private RelatorioVoluntarioService relatorioVoluntarioService;

    @Autowired
    private PdfExportVoluntarioService pdfExportVoluntarioService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadRelatorioVoluntariadoPdf(
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) throws DocumentException, IOException {

        LocalDateTime dataInicioDateTime = dataInicio != null ? dataInicio.atStartOfDay() : null;
        LocalDateTime dataFimDateTime = dataFim != null ? dataFim.atTime(23, 59, 59) : null;

        RelatorioVoluntarioDTO relatorio =
                relatorioVoluntarioService.gerarRelatorioVoluntariadoFiltrado(dataInicioDateTime, dataFimDateTime);

        byte[] pdfBytes = pdfExportVoluntarioService.gerarPdfRelatorioVoluntariado(relatorio);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=relatorio_voluntariado.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
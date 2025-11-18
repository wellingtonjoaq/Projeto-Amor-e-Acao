package projeto_amor_e_acao.TCC.controller;

import org.openpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto_amor_e_acao.TCC.dto.RelatorioEngajamentoDTO;
import projeto_amor_e_acao.TCC.service.PdfExportEngajamentoService;
import projeto_amor_e_acao.TCC.service.RelatorioEngajamentoService;

import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios/engajamento")
public class RelatorioEngajamentoController {

    @Autowired
    private RelatorioEngajamentoService relatorioEngajamentoService;

    @Autowired
    private PdfExportEngajamentoService pdfExportEngajamentoService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadRelatorioEngajamentoPdf()
            throws DocumentException, IOException
    {
        RelatorioEngajamentoDTO relatorio =
                relatorioEngajamentoService.gerarRelatorioEngajamento();

        byte[] pdfBytes =
                pdfExportEngajamentoService.gerarPdfRelatorioEngajamento(relatorio);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                "attachment; filename=relatorio_engajamento.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

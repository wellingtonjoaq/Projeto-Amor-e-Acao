package projeto_amor_e_acao.TCC.controller;

import org.openpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto_amor_e_acao.TCC.dto.RelatorioVoluntarioDTO;
import projeto_amor_e_acao.TCC.service.PdfExportVoluntarioService;
import projeto_amor_e_acao.TCC.service.RelatorioVoluntarioService;

import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios/voluntariado")
public class RelatorioVoluntarioController {

    @Autowired
    private RelatorioVoluntarioService relatorioVoluntarioService;

    @Autowired
    private PdfExportVoluntarioService pdfExportVoluntarioService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadRelatorioVoluntariadoPdf() throws DocumentException, IOException {
        RelatorioVoluntarioDTO relatorio = relatorioVoluntarioService.gerarRelatorioVoluntariado();
        byte[] pdfBytes = pdfExportVoluntarioService.gerarPdfRelatorioVoluntariado(relatorio);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=relatorio_voluntariado.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}


package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto_amor_e_acao.TCC.dto.RelatorioEvasaoDTO;
import projeto_amor_e_acao.TCC.service.PdfExportEvasaoService;
import projeto_amor_e_acao.TCC.service.RelatorioEvasaoService;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios" )
public class RelatorioEvasaoController {

    @Autowired
    private RelatorioEvasaoService relatorioService;
    @Autowired
    private PdfExportEvasaoService pdfExportService;

    @GetMapping("/alunos/exportar/pdf")
    public ResponseEntity<byte[]> exportarRelatorioEvasaoPdf() {
        try {
            List<RelatorioEvasaoDTO> dados = relatorioService.calcularRelatorioEvasao();

            byte[] pdfBytes = pdfExportService.gerarPdfRelatorioEvasao(dados);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            headers.setContentDispositionFormData("attachment", "Relatorio_Evasao_Alunos.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


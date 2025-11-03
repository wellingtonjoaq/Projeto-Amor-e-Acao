package projeto_amor_e_acao.TCC.service;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.RelatorioEvasaoDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class PdfExportEvasaoService {

    public byte[] gerarPdfRelatorioEvasao(List<RelatorioEvasaoDTO> dados)
            throws DocumentException, IOException
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            document.add(new Paragraph("Relatório Estratégico 1: Evasão de Alunos", fontTitulo));
            document.add(new Paragraph("Data de Geração: " + java.time.LocalDate.now() + "\n\n"));
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            table.addCell(new Phrase("Curso"));
            table.addCell(new Phrase("Matriculados"));
            table.addCell(new Phrase("Evasivos"));
            table.addCell(new Phrase("Taxa Evasão (%)"));

            DecimalFormat df = new DecimalFormat("#.##");
            for (RelatorioEvasaoDTO dto : dados) {
                table.addCell(dto.getNomeCurso());
                table.addCell(String.valueOf(dto.getTotalMatriculados()));
                table.addCell(String.valueOf(dto.getTotalEvasivos()));
                table.addCell(df.format(dto.getTaxaEvasao()));
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        }
    }
}

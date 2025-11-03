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
import projeto_amor_e_acao.TCC.model.RelatorioVoluntarioDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

@Service
public class PdfExportVoluntarioService {

    public byte[] gerarPdfRelatorioVoluntariado(RelatorioVoluntarioDTO relatorio)
            throws DocumentException, IOException
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font fontSubTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font fontConteudo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            DecimalFormat df = new DecimalFormat("#.##");

            document.add(new Paragraph("Relatório de Voluntariado", fontTitulo));
            document.add(new Paragraph("Data de Geração: " + LocalDate.now() + "\n\n"));

            document.add(new Paragraph("Voluntários Ativos por Função", fontSubTitulo));
            PdfPTable tabelaFuncao = new PdfPTable(2);

            tabelaFuncao.setWidthPercentage(60);
            tabelaFuncao.addCell(new Phrase("Função", fontSubTitulo));
            tabelaFuncao.addCell(new Phrase("Quantidade", fontSubTitulo));

            for (Map.Entry<String, Long> entry : relatorio.getVoluntariosAtivosPorFuncao().entrySet()) {
                tabelaFuncao.addCell(new Phrase(entry.getKey(), fontConteudo));
                tabelaFuncao.addCell(new Phrase(String.valueOf(entry.getValue()), fontConteudo));
            }

            document.add(tabelaFuncao);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Tempo Médio de Permanência (dias): " +
                    df.format(relatorio.getTempoMedioPermanenciaDias()) + "\n", fontSubTitulo));

            document.add(new Paragraph("Distribuição por Gênero", fontSubTitulo));
            PdfPTable tabelaGenero = new PdfPTable(2);
            tabelaGenero.setWidthPercentage(60);
            tabelaGenero.addCell(new Phrase("Gênero", fontSubTitulo));
            tabelaGenero.addCell(new Phrase("Quantidade", fontSubTitulo));
            for (Map.Entry<String, Long> entry : relatorio.getDistribuicaoPorGenero().entrySet()) {
                tabelaGenero.addCell(new Phrase(entry.getKey(), fontConteudo));
                tabelaGenero.addCell(new Phrase(String.valueOf(entry.getValue()), fontConteudo));
            }
            document.add(tabelaGenero);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Distribuição por Motivação", fontSubTitulo));
            PdfPTable tabelaMotivacao = new PdfPTable(2);

            tabelaMotivacao.setWidthPercentage(60);
            tabelaMotivacao.addCell(new Phrase("Motivação", fontSubTitulo));
            tabelaMotivacao.addCell(new Phrase("Quantidade", fontSubTitulo));

            for (Map.Entry<String, Long> entry : relatorio.getDistribuicaoPorMotivacao().entrySet()) {
                tabelaMotivacao.addCell(new Phrase(entry.getKey(), fontConteudo));
                tabelaMotivacao.addCell(new Phrase(String.valueOf(entry.getValue()), fontConteudo));
            }

            document.add(tabelaMotivacao);

            document.close();

            return out.toByteArray();
        }
    }
}

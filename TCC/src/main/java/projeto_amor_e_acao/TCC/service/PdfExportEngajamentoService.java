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
import projeto_amor_e_acao.TCC.model.RelatorioEngajamentoDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

@Service
public class PdfExportEngajamentoService {

    public byte[] gerarPdfRelatorioEngajamento(RelatorioEngajamentoDTO relatorio)
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

            document.add(
                    new Paragraph(
                    "Relatório Estratégico 3: Engajamento e Desempenho do Sistema",
                    fontTitulo));

            document.add(new Paragraph(
                    "Data de Geração: " + LocalDate.now() + "\n\n"));

            PdfPTable tabelaResumo = new PdfPTable(2);
            tabelaResumo.setWidthPercentage(60);

            tabelaResumo.addCell(new Phrase("Métrica", fontSubTitulo));
            tabelaResumo.addCell(new Phrase("Valor", fontSubTitulo));

            tabelaResumo.addCell("Total de Alunos Cadastrados");
            tabelaResumo.addCell(String.valueOf(relatorio.getTotalAlunos()));

            tabelaResumo.addCell("Total de Voluntários Cadastrados");
            tabelaResumo.addCell(String.valueOf(relatorio.getTotalVoluntarios()));

            tabelaResumo.addCell("Total de Registros");
            tabelaResumo.addCell(String.valueOf(relatorio.getTotalRegistros()));

            tabelaResumo.addCell("Total de Ações Chave (CRUD)");
            tabelaResumo.addCell(String.valueOf(relatorio.getTotalAcoesChave()));

            tabelaResumo.addCell("Taxa de Conversão de Visitante (%)");
            tabelaResumo.addCell(df.format(relatorio.getTaxaConversaoVisitante()));

            document.add(tabelaResumo);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph(
                    "Uso das Funcionalidades Administrativas", fontSubTitulo));

            PdfPTable tabelaUso = new PdfPTable(2);
            tabelaUso.setWidthPercentage(70);

            tabelaUso.addCell(new Phrase("Usuário - Ação", fontSubTitulo));
            tabelaUso.addCell(new Phrase("Contagem", fontSubTitulo));

            for (Map.Entry<String, Long> entry
                    : relatorio.getUsoFuncionalidades().entrySet())
            {
                tabelaUso.addCell(new Phrase(entry.getKey(), fontConteudo));
                tabelaUso.addCell(new Phrase(
                        String.valueOf(entry.getValue()), fontConteudo));
            }

            document.add(tabelaUso);
            document.close();
            return out.toByteArray();
        }
    }
}

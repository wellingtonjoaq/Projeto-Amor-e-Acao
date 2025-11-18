package projeto_amor_e_acao.TCC.service;

import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.dto.RelatorioEvasaoDTO;

import java.awt.*;
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

            Image logo = Image.getInstance(
                    new ClassPathResource("static/imagens/logo.png").getURL());

            logo.scaleToFit(120, 60);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);

            Font fontTituloProjeto = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 20, new Color(0, 70, 140));

            Paragraph tituloProjeto = new Paragraph(
                    "Projeto Amor e Ação", fontTituloProjeto);

            tituloProjeto.setAlignment(Paragraph.ALIGN_CENTER);
            tituloProjeto.setSpacingBefore(10);
            tituloProjeto.setSpacingAfter(20);
            document.add(tituloProjeto);

            Font fontTituloRelatorio =
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

            Paragraph tituloRelatorio = new Paragraph(
                    "Relatório Estratégico 1: Evasão de Alunos", fontTituloRelatorio);

            tituloRelatorio.setAlignment(Paragraph.ALIGN_CENTER);
            tituloRelatorio.setSpacingAfter(10);
            document.add(tituloRelatorio);

            Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.GRAY);
            Paragraph dataGeracao = new Paragraph(
                    "Data de Geração: " + java.time.LocalDate.now(), fontData);

            dataGeracao.setAlignment(Paragraph.ALIGN_CENTER);
            dataGeracao.setSpacingAfter(20);
            document.add(dataGeracao);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{3, 2, 2, 2});

            Font fontCabecalho = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Color corCabecalho = new Color(0, 121, 182);

            String[] headers = {"Curso", "Matriculados", "Evasivos", "Taxa Evasão (%)"};
            for (String headerTitle : headers) {
                PdfPCell header = new PdfPCell(new Phrase(headerTitle, fontCabecalho));
                header.setBackgroundColor(corCabecalho);
                header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                header.setPadding(8);
                table.addCell(header);
            }

            Font fontCelula = FontFactory.getFont(FontFactory.HELVETICA, 11);
            DecimalFormat df = new DecimalFormat("#.##");

            for (RelatorioEvasaoDTO dto : dados) {
                PdfPCell cell1 = new PdfPCell(
                        new Phrase(dto.getNomeCurso(), fontCelula));

                PdfPCell cell2 = new PdfPCell(
                        new Phrase(String.valueOf(dto.getTotalMatriculados()), fontCelula));

                PdfPCell cell3 = new PdfPCell(
                        new Phrase(String.valueOf(dto.getTotalEvasivos()), fontCelula));

                PdfPCell cell4 = new PdfPCell(
                        new Phrase(df.format(dto.getTaxaEvasao()), fontCelula));

                cell1.setPadding(6);
                cell2.setPadding(6);
                cell3.setPadding(6);
                cell4.setPadding(6);

                cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
            }

            document.add(table);

            document.close();
            return out.toByteArray();
        }
    }
}

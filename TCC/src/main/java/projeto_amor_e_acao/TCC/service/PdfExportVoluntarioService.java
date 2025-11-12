package projeto_amor_e_acao.TCC.service;

import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.dto.RelatorioVoluntarioDTO;

import java.awt.*;
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

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

            Paragraph tituloRelatorio = new Paragraph(
                    "Relatório de Voluntariado", fontTitulo);
            tituloRelatorio.setAlignment(Paragraph.ALIGN_CENTER);
            tituloRelatorio.setSpacingAfter(10);
            document.add(tituloRelatorio);

            Font fontData = FontFactory.getFont(
                    FontFactory.HELVETICA, 12, Color.GRAY);

            Paragraph dataGeracao = new Paragraph(
                    "Data de Geração: " + LocalDate.now(), fontData);
            dataGeracao.setAlignment(Paragraph.ALIGN_CENTER);
            dataGeracao.setSpacingAfter(20);
            document.add(dataGeracao);

            Font fontSubTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font fontConteudo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            DecimalFormat df = new DecimalFormat("#.##");

            Paragraph subtituloFuncao = new Paragraph(
                    "Voluntários Ativos por Função", fontSubTitulo);
            subtituloFuncao.setSpacingAfter(10);
            document.add(subtituloFuncao);

            PdfPTable tabelaFuncao = new PdfPTable(2);
            tabelaFuncao.setWidthPercentage(60);

            PdfPCell cabecalhoFuncao1 = new PdfPCell(
                    new Phrase("Função", fontSubTitulo));
            cabecalhoFuncao1.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoFuncao1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoFuncao1.setPadding(8);

            PdfPCell cabecalhoFuncao2 = new PdfPCell(
                    new Phrase("Quantidade", fontSubTitulo));
            cabecalhoFuncao2.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoFuncao2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoFuncao2.setPadding(8);

            tabelaFuncao.addCell(cabecalhoFuncao1);
            tabelaFuncao.addCell(cabecalhoFuncao2);

            for (Map.Entry<String, Long> entry :
                    relatorio.getVoluntariosAtivosPorFuncao().entrySet())
            {
                PdfPCell cellFuncao = new PdfPCell(
                        new Phrase(entry.getKey(), fontConteudo));

                PdfPCell cellQuantidade = new PdfPCell(
                        new Phrase(String.valueOf(entry.getValue()), fontConteudo));

                cellFuncao.setPadding(6);
                cellQuantidade.setPadding(6);
                cellQuantidade.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                tabelaFuncao.addCell(cellFuncao);
                tabelaFuncao.addCell(cellQuantidade);
            }

            document.add(tabelaFuncao);

            document.add(new Paragraph("\n"));

            Paragraph subtituloGenero = new Paragraph(
                    "Distribuição por Gênero", fontSubTitulo);
            subtituloGenero.setSpacingAfter(10);
            document.add(subtituloGenero);

            PdfPTable tabelaGenero = new PdfPTable(2);
            tabelaGenero.setWidthPercentage(60);

            PdfPCell cabecalhoGenero1 = new PdfPCell(
                    new Phrase("Gênero", fontSubTitulo));
            cabecalhoGenero1.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoGenero1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoGenero1.setPadding(8);

            PdfPCell cabecalhoGenero2 = new PdfPCell(
                    new Phrase("Quantidade", fontSubTitulo));
            cabecalhoGenero2.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoGenero2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoGenero2.setPadding(8);

            tabelaGenero.addCell(cabecalhoGenero1);
            tabelaGenero.addCell(cabecalhoGenero2);

            for (Map.Entry<String, Long> entry :
                    relatorio.getDistribuicaoPorGenero().entrySet())
            {
                PdfPCell cellGenero = new PdfPCell(
                        new Phrase(entry.getKey(), fontConteudo));

                PdfPCell cellQuantidade = new PdfPCell(
                        new Phrase(String.valueOf(entry.getValue()), fontConteudo));

                cellGenero.setPadding(6);
                cellQuantidade.setPadding(6);
                cellQuantidade.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                tabelaGenero.addCell(cellGenero);
                tabelaGenero.addCell(cellQuantidade);
            }

            document.add(tabelaGenero);

            document.add(new Paragraph("\n"));

            Paragraph subtituloMotivacao = new Paragraph(
                    "Distribuição por Motivação", fontSubTitulo);
            subtituloMotivacao.setSpacingAfter(10);
            document.add(subtituloMotivacao);

            PdfPTable tabelaMotivacao = new PdfPTable(2);
            tabelaMotivacao.setWidthPercentage(60);

            PdfPCell cabecalhoMotivacao1 = new PdfPCell(
                    new Phrase("Motivação", fontSubTitulo));
            cabecalhoMotivacao1.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoMotivacao1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoMotivacao1.setPadding(8);

            PdfPCell cabecalhoMotivacao2 = new PdfPCell(
                    new Phrase("Quantidade", fontSubTitulo));
            cabecalhoMotivacao2.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoMotivacao2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoMotivacao2.setPadding(8);

            tabelaMotivacao.addCell(cabecalhoMotivacao1);
            tabelaMotivacao.addCell(cabecalhoMotivacao2);

            for (Map.Entry<String, Long> entry :
                    relatorio.getDistribuicaoPorMotivacao().entrySet())
            {
                PdfPCell cellMotivacao = new PdfPCell(
                        new Phrase(entry.getKey(), fontConteudo));

                PdfPCell cellQuantidade = new PdfPCell(
                        new Phrase(String.valueOf(entry.getValue()), fontConteudo));

                cellMotivacao.setPadding(6);
                cellQuantidade.setPadding(6);
                cellQuantidade.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                tabelaMotivacao.addCell(cellMotivacao);
                tabelaMotivacao.addCell(cellQuantidade);
            }

            document.add(tabelaMotivacao);

            document.close();
            return out.toByteArray();
        }
    }
}

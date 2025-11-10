package projeto_amor_e_acao.TCC.service;

import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import projeto_amor_e_acao.TCC.model.RelatorioEngajamentoDTO;

import java.awt.*;
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
                    "Relatório Estratégico 3: Engajamento e Desempenho do Sistema",
                    fontTitulo);

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

            PdfPTable tabelaResumo = new PdfPTable(2);
            tabelaResumo.setWidthPercentage(60);

            PdfPCell cabecalho1 = new PdfPCell(
                    new Phrase("Métrica", fontSubTitulo));
            cabecalho1.setBackgroundColor(new Color(0, 121, 182));
            cabecalho1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalho1.setPadding(8);

            PdfPCell cabecalho2 = new PdfPCell(
                    new Phrase("Valor", fontSubTitulo));
            cabecalho2.setBackgroundColor(new Color(0, 121, 182));
            cabecalho2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalho2.setPadding(8);

            tabelaResumo.addCell(cabecalho1);
            tabelaResumo.addCell(cabecalho2);

            tabelaResumo.addCell(new Phrase(
                    "Total de Alunos Cadastrados", fontConteudo));
            tabelaResumo.addCell(new Phrase(
                    String.valueOf(relatorio.getTotalAlunos()), fontConteudo));

            tabelaResumo.addCell(new Phrase(
                    "Total de Voluntários Cadastrados", fontConteudo));
            tabelaResumo.addCell(new Phrase(
                    String.valueOf(relatorio.getTotalVoluntarios()), fontConteudo));

            tabelaResumo.addCell(new Phrase(
                    "Total de Registros", fontConteudo));
            tabelaResumo.addCell(new Phrase(
                    String.valueOf(relatorio.getTotalRegistros()), fontConteudo));

            tabelaResumo.addCell(new Phrase(
                    "Total de Ações Chave (CRUD)", fontConteudo));
            tabelaResumo.addCell(new Phrase(
                    String.valueOf(relatorio.getTotalAcoesChave()), fontConteudo));

            tabelaResumo.addCell(new Phrase(
                    "Taxa de Conversão de Visitante (%)", fontConteudo));
            tabelaResumo.addCell(new Phrase(
                    df.format(relatorio.getTaxaConversaoVisitante()), fontConteudo));

            document.add(tabelaResumo);
            document.add(new Paragraph("\n"));

            Paragraph subtituloUso = new Paragraph(
                    "Uso das Funcionalidades Administrativas", fontSubTitulo);
            subtituloUso.setSpacingAfter(10);
            document.add(subtituloUso);

            PdfPTable tabelaUso = new PdfPTable(2);
            tabelaUso.setWidthPercentage(70);

            PdfPCell cabecalhoUso1 = new PdfPCell(
                    new Phrase("Usuário - Ação", fontSubTitulo));
            cabecalhoUso1.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoUso1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoUso1.setPadding(8);

            PdfPCell cabecalhoUso2 = new PdfPCell(
                    new Phrase("Contagem", fontSubTitulo));
            cabecalhoUso2.setBackgroundColor(new Color(0, 121, 182));
            cabecalhoUso2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cabecalhoUso2.setPadding(8);

            tabelaUso.addCell(cabecalhoUso1);
            tabelaUso.addCell(cabecalhoUso2);

            for (Map.Entry<String, Long> entry :
                    relatorio.getUsoFuncionalidades().entrySet())
            {
                PdfPCell userAcao = new PdfPCell(
                        new Phrase(entry.getKey(), fontConteudo));

                PdfPCell contagem = new PdfPCell(
                        new Phrase(String.valueOf(entry.getValue()), fontConteudo));

                userAcao.setPadding(6);
                contagem.setPadding(6);
                contagem.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                tabelaUso.addCell(userAcao);
                tabelaUso.addCell(contagem);
            }

            document.add(tabelaUso);

            document.close();
            return out.toByteArray();
        }
    }
}

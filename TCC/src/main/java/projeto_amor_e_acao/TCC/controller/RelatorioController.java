package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projeto_amor_e_acao.TCC.model.Usuario;
import projeto_amor_e_acao.TCC.service.UsuarioService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("relatorio")
public class RelatorioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public String mostrarPaginaListar(Model model) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        model.addAttribute("usuarioLogado", usuario);
        return "administrativo/relatorio/listar";
    }

    @GetMapping("/listarUsuarioSimples")
    public String mostrarPaginaListarUsuarioSimples(Model model) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        model.addAttribute("usuarioLogado", usuario);
        return "usuario-simples/relatorio/listar";
    }

    @GetMapping("/relatorios/alunos/pdf")
    public ResponseEntity<InputStreamResource> exportarRelatorioAlunos()
            throws IOException
    {
        return baixarArquivo("caminho/para/relatorio_alunos.pdf",
                "relatorio_alunos.pdf");
    }

    @GetMapping("/relatorios/voluntariado/pdf")
    public ResponseEntity<InputStreamResource> exportarRelatorioVoluntariado()
            throws IOException
    {
        return baixarArquivo("caminho/para/relatorio_voluntariado.pdf",
                "relatorio_voluntariado.pdf");
    }

    @GetMapping("/relatorios/engajamento/pdf")
    public ResponseEntity<InputStreamResource> exportarRelatorioEngajamento()
            throws IOException
    {
        return baixarArquivo("caminho/para/relatorio_engajamento.pdf",
                "relatorio_engajamento.pdf");
    }

    private ResponseEntity<InputStreamResource> baixarArquivo(
            String caminhoArquivo, String nomeDownload) throws IOException
    {
        File arquivo = new File(caminhoArquivo);

        if(!arquivo.exists())
        {
            throw new FileNotFoundException(
                    "Arquivo não encontrado: " + caminhoArquivo);
        }

        InputStreamResource recurso =
                new InputStreamResource(new FileInputStream(arquivo));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + nomeDownload)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(arquivo.length()).body(recurso);
    }
}

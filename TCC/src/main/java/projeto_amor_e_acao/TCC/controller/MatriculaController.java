package projeto_amor_e_acao.TCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projeto_amor_e_acao.TCC.model.Matricula;
import projeto_amor_e_acao.TCC.service.MatriculaService;

@Controller
@RequestMapping("matricula")
public class MatriculaController {

    @Autowired
    private MatriculaService service;

    @GetMapping()
    public String iniciar(Matricula matricula, Model model) {
        return "matricula/formulario";
    }

    @PostMapping()
    public String inserir(Matricula matricula, Model model) {
        return iniciar(matricula, model);
    }

    @PostMapping("salvar")
    public String salvar(Matricula matricula, Model model) {
        try {
            service.salvar(matricula);
            return "redirect:/matricula/listar";
        } catch (Exception e) {
            model.addAttribute("erro", "Algo de errado não deu certo: ");
            return iniciar(matricula, model);
        }
    }

    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("matriculas", service.listarTodos());
        return "matricula/lista";
    }

    @GetMapping("editar/{id}")
    public String alterar(@PathVariable Long id, Model model) {
        model.addAttribute("matricula", service.buscarPorId(id));
        return "matricula/formulario";
    }

    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id, Model model) {
        service.deletarPorId(id);
        return "redirect:/matricula/listar";
    }
}

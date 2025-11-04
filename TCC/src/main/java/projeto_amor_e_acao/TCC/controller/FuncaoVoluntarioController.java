package projeto_amor_e_acao.TCC.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projeto_amor_e_acao.TCC.model.Aluno;
import projeto_amor_e_acao.TCC.model.FuncaoVoluntario;
import projeto_amor_e_acao.TCC.model.Voluntario;
import projeto_amor_e_acao.TCC.service.FuncaoVoluntarioService;

import java.util.List;

@Controller
@RequestMapping("funcao")
public class FuncaoVoluntarioController {

    @Autowired
    private FuncaoVoluntarioService service;

    @GetMapping("listar")
    public String listar(Model model) {
        model.addAttribute("funcoes", service.listarTodos());
        return "funcao/listar";
    }

    @PostMapping("salvar")
    public String salvar(@RequestParam("funcoesSelecionadas") String funcoesSelecionadas, Model model) {

        try {
            List<String> funcoes = List.of(funcoesSelecionadas.split(","));

            for (String nome : funcoes) {
                if (nome == null || nome.isBlank()) continue;
                FuncaoVoluntario funcao = new FuncaoVoluntario(null, nome.trim());
                service.salvar(funcao);
            }

            return "redirect:/funcao/listar";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "funcao/listar";
        }
    }



    @GetMapping("remover/{id}")
    public String remover(@PathVariable Long id) {

        service.deletarPorId(id);
        return "redirect:/funcao/listar";
    }
}

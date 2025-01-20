package br.com.teste.cast.controller;

import br.com.teste.cast.dto.UsuarioDTO;
import br.com.teste.cast.model.Usuario;
import br.com.teste.cast.service.impl.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/formCriarUsuario")
    public String exibirFormularioCriacaoUsuarios() {
        return "usuarios/criar";
    }

    @PostMapping("/salvar")
    public ResponseEntity<String> criarPessoa(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            return ResponseEntity.ok(usuarioService.salvarUsuario(usuarioDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar conta: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public String listarUsuarios(Model model) {

        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
             model.addAttribute("usuarios", usuarios);
            return "usuarios/listar";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao listar Usuários: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}")
    public String buscarPessoaPorId(@PathVariable Long id, Model model) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(id);
            if (usuario == null) {
                model.addAttribute("erro", "Pessoa não encontrada");
                return "error";
            }
            model.addAttribute("usuario", usuario);
            return "pessoas/editar";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao buscar pessoa: " + e.getMessage());
            return "error";
        }
    }


    @PostMapping("/deletar/{id}")
    public ResponseEntity<String> deletarPessoa(@PathVariable Integer id) {
        try {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.ok("Pessoa deletada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar a pessoa: " + e.getMessage());
        }
    }
}

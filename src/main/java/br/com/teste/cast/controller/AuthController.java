package br.com.teste.cast.controller;

import br.com.teste.cast.constantes.PerfilUsuario;
import br.com.teste.cast.model.Usuario;
import br.com.teste.cast.service.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "loginUsuario";
    }

    @GetMapping("/naoAutorizado")
    public String loginNaoAutorizado() {
        return "naoAutorizado";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model, HttpServletRequest request) {
        System.out.println("Tentando login com usuário: " + username);

        Usuario usuario = authService.autenticar(username, password, request);

        if (usuario != null) {
            if (usuario.getPerfilUsuario() == PerfilUsuario.ADMIN) {
                return "redirect:/admin/home";
            } else {
                return "redirect:/contas/listar";
            }
        } else {

            model.addAttribute("error", "Usuário ou senha inválidos");
            return "loginUsuario";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
          session.invalidate();
    SecurityContextHolder.clearContext();
        return "redirect:/auth/login";
    }

}

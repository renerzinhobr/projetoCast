package br.com.teste.cast.controller;

import br.com.teste.cast.constantes.PerfilUsuario;
import br.com.teste.cast.service.IAuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private HttpSession session;

    @Autowired
    private IAuthService authService;

    @RolesAllowed("ADMIN")
    @GetMapping("/admin/home")
    public String adminHome() {
            return "/admin/admin-home";
           }


}


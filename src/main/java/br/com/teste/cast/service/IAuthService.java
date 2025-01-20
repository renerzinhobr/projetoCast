package br.com.teste.cast.service;

import br.com.teste.cast.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {
    public Usuario autenticar(String login, String senha, HttpServletRequest request);
    public boolean VerificaRoleNaSession(HttpSession session, String role);
}

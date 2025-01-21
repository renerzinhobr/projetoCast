package br.com.teste.cast.service.impl;

import br.com.teste.cast.model.Usuario;
import br.com.teste.cast.repository.UsuarioRepository;
import br.com.teste.cast.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthService implements IAuthService,UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
            Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

               List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getPerfilUsuario() != null) {

            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfilUsuario().name()));
        }

            return new User(
                usuario.getLogin(),
                usuario.getSenha(),
                authorities
        );
    }

    @Override
    public Usuario autenticar(String login, String senha, HttpServletRequest request) {

        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new BadCredentialsException("Senha incorreta");
        }

        autenticarUsuarioNaSessao(usuario,request);

       return usuario;
    }


    private void autenticarUsuarioNaSessao(Usuario usuario, HttpServletRequest request) {

        String perfil = usuario.getPerfilUsuario().name();


        User principal = new User(usuario.getLogin(), usuario.getSenha(),
                List.of(new SimpleGrantedAuthority("ROLE_" + perfil)));


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, usuario.getSenha(), principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);


        HttpSession session = request.getSession();
        session.setAttribute("user", principal);
        session.setAttribute("perfil", perfil);
        session.setAttribute("usuario", usuario);

    }

    @Override
    public boolean VerificaRoleNaSession(HttpSession session, String role) {
            User user = (User) session.getAttribute("user");

        if (user != null) {
                Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            return authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals(role));
        }
        return false;
    }



}
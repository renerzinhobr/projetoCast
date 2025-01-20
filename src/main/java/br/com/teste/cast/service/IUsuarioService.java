package br.com.teste.cast.service;

import br.com.teste.cast.dto.UsuarioDTO;
import br.com.teste.cast.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUsuarioService {

       public String salvarUsuario(UsuarioDTO usuarioDTO);

    public List<Usuario> listarUsuarios();

    public Usuario buscarUsuarioPorId(Long idUsuario);

    public void deletarUsuario(Integer idUsuario);
}

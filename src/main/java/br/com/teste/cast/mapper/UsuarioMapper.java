package br.com.teste.cast.mapper;

import br.com.teste.cast.dto.UsuarioDTO;
import br.com.teste.cast.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .ativo(usuario.getAtivo())
                .login(usuario.getLogin())
                .nome(usuario.getNome())
                .cpfCnpj(usuario.getCpfCnpj())
                .email(usuario.getEmail())
                .perfilUsuario(usuario.getPerfilUsuario())
                .dataHoraInclusao(usuario.getDataHoraInclusao())
                .build();
    }

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(usuarioDTO.getIdUsuario());
        usuario.setAtivo(usuarioDTO.getAtivo());
        usuario.setLogin(usuarioDTO.getLogin());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpfCnpj(usuarioDTO.getCpfCnpj());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPerfilUsuario(usuarioDTO.getPerfilUsuario());
        usuario.setDataHoraInclusao(usuarioDTO.getDataHoraInclusao());

        return usuario;
    }
}

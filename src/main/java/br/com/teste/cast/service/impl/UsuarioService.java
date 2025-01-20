package br.com.teste.cast.service.impl;

import br.com.teste.cast.dto.UsuarioDTO;
import br.com.teste.cast.mapper.UsuarioMapper;
import br.com.teste.cast.model.Usuario;
import br.com.teste.cast.repository.UsuarioRepository;
import br.com.teste.cast.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

@Override
    public String salvarUsuario(UsuarioDTO usuarioDTO) {
        String retorno;
        if (usuarioDTO.getCpfCnpj() == null || usuarioDTO.getCpfCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo Cpf/Cnpj deve ser preenchido .");
        }

        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo email deve ser preenchido .");
        }

        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo Nome deve ser preenchido .");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    usuario.setAtivo(Byte.valueOf("1"));
    usuario.setSenha(encoder.encode(usuarioDTO.getSenha()));
      Optional<Usuario> usuarioExistente = usuarioRepository.findByCpfCnpj(usuarioDTO.getCpfCnpj());
        if (usuarioExistente.isPresent()) {
            usuario.setIdUsuario(usuarioExistente.get().getIdUsuario());
            retorno = "Usuário Alterado com Sucesso!";
        }else {
            retorno = "Usuário Criado com Sucesso!";
        }

        usuario.setDataHoraInclusao(LocalDateTime.now());
        usuarioRepository.save(usuario);
     return  retorno;

    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarUsuarioPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario.intValue()).orElse(null);
    }

   @Override
    public void deletarUsuario(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}


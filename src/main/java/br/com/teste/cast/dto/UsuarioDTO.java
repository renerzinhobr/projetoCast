package br.com.teste.cast.dto;

import br.com.teste.cast.constantes.PerfilUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long idUsuario;
    private Byte ativo;
    private String login;
    private String senha;
    private PerfilUsuario perfilUsuario;
    private String nome;
    private String cpfCnpj;
    private String email;
    private LocalDateTime dataHoraInclusao;
}

package br.com.teste.cast.model;

import br.com.teste.cast.constantes.PerfilUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = "LOGIN"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Long idUsuario;

    @Column(name = "ATIVO", nullable = false)
    private Byte ativo = 1;

    @Column(name = "LOGIN", length = 45, nullable = false, unique = true)
    private String login;

    @Column(name = "SENHA", length = 200, nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERFIL", nullable = false, length = 20)
    private PerfilUsuario perfilUsuario;

    @Column(name = "NOME", length = 100, nullable = false)
    private String nome;

    @Column(name = "CPFCNPJ", length = 45, nullable = false)
    private String cpfCnpj;

    @Column(name = "EMAIL", length = 100, nullable = true)
    private String email;

    @Column(name = "DATAHORAINCLUSAO", nullable = false)
    private LocalDateTime dataHoraInclusao;

     @Override
    public List<SimpleGrantedAuthority> getAuthorities() {

        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + perfilUsuario.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return ativo != null && ativo == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo != null && ativo == 1;
    }
}

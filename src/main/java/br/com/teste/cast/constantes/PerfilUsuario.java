package br.com.teste.cast.constantes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerfilUsuario {

    ADMIN("ADMIN","ROLE_ADMIN"),
    CORRENTISTA("CORRENTISTA","ROLE_CORRENTISTA");

    private final String descricao;
    private final String role;

     public static PerfilUsuario fromString(String descricao) {
        for (PerfilUsuario perfil : PerfilUsuario.values()) {
            if (perfil.getDescricao().equalsIgnoreCase(descricao)) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Valor inválido para PerfilUsuario: " + descricao);
    }

    @Override
    public String toString() {
        return descricao;
    }

}

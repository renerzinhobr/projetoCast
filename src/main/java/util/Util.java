package util;

public class Util {
    public static void validarValor(Double valor, String mensagemErro) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }
}

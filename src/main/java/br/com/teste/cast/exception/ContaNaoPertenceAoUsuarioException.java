package br.com.teste.cast.exception;

public class ContaNaoPertenceAoUsuarioException extends RuntimeException {
    public ContaNaoPertenceAoUsuarioException(String message) {
        super(message);
    }
}

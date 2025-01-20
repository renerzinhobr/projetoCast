package br.com.teste.cast.constantes;

public enum TipoPagamento {
    CREDITO("Crédito"),
    DEBITO("Débito"),
    TRANSFERENCIA("Transferência");

    private final String descricao;

    TipoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

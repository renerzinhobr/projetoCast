package br.com.teste.cast.constantes;

public enum Mensagem {

    CONTA_SALVA("Conta salva com sucesso!"),
    CREDITO_EFETIVADO("Crédito efetivado com sucesso!"),
    DEBITO_EFETIVADO("Débito efetivado com sucesso!"),
    TRANSFERENCIA_EFETIVADA("Transferência efetivada com sucesso!"),
    ERRO_VALOR_INVALIDO("Valor inválido para Operação."),


    ERRO_SALVAR_CONTA("Erro ao salvar conta: "),
    ERRO_CREDITAR_CONTA("Erro ao creditar na conta: "),
    ERRO_DEBITAR_CONTA("Erro ao debitar na conta: "),
    ERRO_TRANSFERIR_CONTA("Erro ao tentar transferir na conta: "),
    ERRO_CONTA_NAO_CADASTRADA("Conta não encontrada."),
    ERRO_CONTA_DESTINO_NAO_CADASTRADA("Conta destino não encontrada."),
    ERRO_CONTA_ORIGEM_NAO_CADASTRADA("Conta origem não encontrada."),
    ERRO_CONTA_ORIGEM_E_DESTINO_SAO_IGUAIS("Conta origem e destino são iguais."),
    ERRO_CONTA_SADO_INSUFICIENTE("Saldo insuficiente! Saldo atual:"),
    ERRO_CONTA_DEVE_USUARIO_VALIDO("A conta deve estar associada a um usuário válido."),
    ERRO_CONTA_USUARIO_ASSIADO_NAO_ENCONTRADO("Usuário associado  à conta não encontrado."),
    ERRO_NOME_USUARIO_OBRIGATORIO("Nome Usuário é  obrigatório."),
    ERRO_USUARIO_TITULAR_NAO_ENCONTRADO("Usuário titular da Conta não Encontrado na Base de dados."),
    ERRO_USUARIO_NAO_AUTORIZADO("Usuário não autorizado! Precisa se logar!"),


    ERRO_CONTA_NAO_PERTENCE_USUARIO_LOGADO("Conta não pertence ao usuário Logado!");

    private final String texto;

    Mensagem(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}

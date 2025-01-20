package br.com.teste.cast.service.impl;

import br.com.teste.cast.constantes.TipoPagamento;
import br.com.teste.cast.model.Conta;
import br.com.teste.cast.model.Transacao;
import br.com.teste.cast.repository.TransacaoRepository;
import br.com.teste.cast.service.ITransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransacaoService implements ITransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Override
    public void registrarTransacao(Conta conta, TipoPagamento tipoPagamento, BigDecimal valor) {
        Transacao transacao = new Transacao(conta, tipoPagamento, valor);
        transacaoRepository.save(transacao);
    }
}

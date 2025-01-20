package br.com.teste.cast.service;

import br.com.teste.cast.constantes.TipoPagamento;
import br.com.teste.cast.model.Conta;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public interface ITransacaoService {
    public void registrarTransacao(Conta conta, TipoPagamento tipoPagamento, BigDecimal valor);
}

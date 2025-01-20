package br.com.teste.cast.service;

import br.com.teste.cast.dto.ContaDTO;
import br.com.teste.cast.model.Conta;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IContaService {
     Conta salvarConta(ContaDTO contaDTO);

     List<Conta> buscarTodasContas();

     void creditar(Long id, double valor);

     void debitar(Long id, double valor);

    void transferir(Long idOrigem, Long idDestino, double valor,HttpSession session);

    Conta buscarContaPorId(Long id);
    Conta buscarContaPorIdParaOperacao(Long id,HttpSession session);
    Conta salvar(Conta conta);

    List<Conta> buscarTodasContasUsuario(HttpSession session);
}

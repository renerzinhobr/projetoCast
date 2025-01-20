package br.com.teste.cast.service.impl;

import br.com.teste.cast.constantes.Mensagem;
import br.com.teste.cast.constantes.TipoPagamento;
import br.com.teste.cast.dto.ContaDTO;
import br.com.teste.cast.exception.ContaNaoPertenceAoUsuarioException;
import br.com.teste.cast.mapper.ContaMapper;
import br.com.teste.cast.model.Conta;
import br.com.teste.cast.model.Usuario;
import br.com.teste.cast.repository.ContaRepository;
import br.com.teste.cast.service.IContaService;
import br.com.teste.cast.service.ITransacaoService;
import br.com.teste.cast.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService implements IContaService {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    ITransacaoService transacaoService;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaMapper contaMapper;


    public Conta salvarConta(ContaDTO contaDTO) {


        if (contaDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException(Mensagem.ERRO_NOME_USUARIO_OBRIGATORIO.getTexto());
        }


        Usuario usuario = usuarioService.buscarUsuarioPorId(contaDTO.getIdUsuario());
        if (usuario == null) {
            throw new IllegalArgumentException(Mensagem.ERRO_USUARIO_TITULAR_NAO_ENCONTRADO.getTexto());
        }
        else {
            contaDTO.setUsuario(usuario);
        }

        Conta conta = contaMapper.toEntity(contaDTO);
        conta.setSaldo(BigDecimal.ZERO);
        return contaRepository.save(conta);

    }




    @Override
    public List<Conta> buscarTodasContas() {
        return contaRepository.findAll();
    }

    @Override
    @Transactional
    public void creditar(Long id, double valor) {
        Conta conta = contaRepository.findByIdWithLock(id).orElseThrow(() -> new IllegalArgumentException(Mensagem.ERRO_CONTA_NAO_CADASTRADA.getTexto()));
        conta.creditar(BigDecimal.valueOf(valor));
        transacaoService.registrarTransacao(conta, TipoPagamento.CREDITO, BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Transactional
    public void debitar(Long id, double valor) {
        Conta conta = contaRepository.findByIdWithLock(id).orElseThrow(() -> new IllegalArgumentException(Mensagem.ERRO_CONTA_NAO_CADASTRADA.getTexto()));
        conta.debitar(BigDecimal.valueOf(valor));
        transacaoService.registrarTransacao(conta, TipoPagamento.DEBITO, BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Transactional
    public void transferir(Long idOrigem, Long idDestino, double valor, HttpSession session) {
        if (idOrigem.equals(idDestino)) {
            throw new IllegalArgumentException(Mensagem.ERRO_CONTA_ORIGEM_E_DESTINO_SAO_IGUAIS.getTexto());
        }

        Conta contaOrigem = contaRepository.findByIdWithLock(idOrigem)
                .orElseThrow(() -> new IllegalArgumentException(Mensagem.ERRO_CONTA_ORIGEM_NAO_CADASTRADA.getTexto()));

        Conta contaDestino = contaRepository.findByIdWithLock(idDestino)
                .orElseThrow(() -> new IllegalArgumentException(Mensagem.ERRO_CONTA_DESTINO_NAO_CADASTRADA.getTexto()));

        contaOrigem = buscarContaPorIdParaOperacao(contaOrigem.getId(), session);

        if (contaOrigem.getSaldo().compareTo(BigDecimal.valueOf(valor)) < 0) {
            throw new IllegalArgumentException(Mensagem.ERRO_CONTA_SADO_INSUFICIENTE.getTexto().concat(contaOrigem.getSaldo().toString()));
        }

        contaOrigem.transferir(contaDestino, BigDecimal.valueOf(valor));

        transacaoService.registrarTransacao(
                contaOrigem,
                TipoPagamento.TRANSFERENCIA,
                BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP)
        );

        transacaoService.registrarTransacao(
                contaDestino,
                TipoPagamento.CREDITO,
                BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Override
    public Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(Mensagem.ERRO_CONTA_NAO_CADASTRADA.getTexto()));
    }

    @Override
    public Conta buscarContaPorIdParaOperacao(Long idConta,HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return verificarSeContaPertenceAoUsuario(usuario.getIdUsuario().longValue(), idConta);
   }

    @Override
    public Conta salvar(Conta conta) {

         if (conta.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException(Mensagem.ERRO_CONTA_DEVE_USUARIO_VALIDO.getTexto());
        }

        Usuario usuario = usuarioService.buscarUsuarioPorId(conta.getUsuario().getIdUsuario());
        if (usuario == null) {
            throw new IllegalArgumentException(Mensagem.ERRO_CONTA_USUARIO_ASSIADO_NAO_ENCONTRADO.getTexto());
        }

        conta.setUsuario(usuario);

        return contaRepository.save(conta);
    }

    @Override
    public List<Conta> buscarTodasContasUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return contaRepository.findAllByUsuario_IdUsuario(usuario.getIdUsuario().longValue());

    }

    public Conta verificarSeContaPertenceAoUsuario(Long usuarioId, Long idConta) {
        return contaRepository.findByUsuario_IdUsuarioAndId(usuarioId, idConta)
                .orElseThrow(() -> new ContaNaoPertenceAoUsuarioException(
                        Mensagem.ERRO_CONTA_NAO_PERTENCE_USUARIO_LOGADO.getTexto()));
    }


}



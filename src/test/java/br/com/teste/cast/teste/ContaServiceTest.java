package br.com.teste.cast.teste;

import br.com.teste.cast.constantes.Mensagem;
import br.com.teste.cast.constantes.PerfilUsuario;
import br.com.teste.cast.dto.ContaDTO;
import br.com.teste.cast.model.Conta;
import br.com.teste.cast.model.Usuario;
import br.com.teste.cast.repository.ContaRepository;
import br.com.teste.cast.service.ITransacaoService;
import br.com.teste.cast.service.IUsuarioService;
import br.com.teste.cast.service.impl.ContaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private IUsuarioService usuarioService;

    @Mock
    private ITransacaoService transacaoService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Usuario usuario;

    @Mock
    private Conta contaOrigem;

    @Mock
    private Conta contaDestino;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarContaUsuarioNaoInformado() {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setIdUsuario(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.salvarConta(contaDTO);
        });

        assertEquals(Mensagem.ERRO_NOME_USUARIO_OBRIGATORIO.getTexto(), exception.getMessage());
    }

    @Test
    void testCriarContaUsuarioNaoEncontrado() {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setIdUsuario(1L);

        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.salvarConta(contaDTO);
        });

        assertEquals(Mensagem.ERRO_USUARIO_TITULAR_NAO_ENCONTRADO.getTexto(), exception.getMessage());
    }

    @Test
    void testCreditarContaNaoEncontrada() {
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.creditar(1L, 100.0);
        });

        assertEquals(Mensagem.ERRO_CONTA_NAO_CADASTRADA.getTexto(), exception.getMessage());
    }

    @Test
    void testCreditar() {
        Conta conta = new Conta();
        conta.setSaldo(BigDecimal.ZERO);
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.of(conta));

        contaService.creditar(1L, 100.0);

        assertEquals(BigDecimal.valueOf(100.0).setScale(2, RoundingMode.HALF_UP), conta.getSaldo().setScale(2, RoundingMode.HALF_UP));
        verify(transacaoService, times(1)).registrarTransacao(any(Conta.class), any(), any());
    }

    @Test
    void testTransferirContaOrigemNaoEncontrada() {
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.transferir(1L, 2L, 50.0, session);
        });

        assertEquals(Mensagem.ERRO_CONTA_ORIGEM_NAO_CADASTRADA.getTexto(), exception.getMessage());
    }

    @Test
    void testTransferirContaDestinoNaoEncontrada() {
        Conta contaOrigem = new Conta();
        contaOrigem.setSaldo(BigDecimal.valueOf(100.0));
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdWithLock(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.transferir(1L, 2L, 50.0, session);
        });

        assertEquals(Mensagem.ERRO_CONTA_DESTINO_NAO_CADASTRADA.getTexto(), exception.getMessage());
    }

    @Test
    void testTransferirSaldoInsuficiente() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

         when(request.getSession()).thenReturn(sessionMock);

        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(123L);
        when(usuarioMock.getLogin()).thenReturn("user123");
        when(usuarioMock.getPerfilUsuario()).thenReturn(PerfilUsuario.CORRENTISTA);

        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);


        Conta contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setUsuario(usuarioMock);
        contaOrigem.setSaldo(BigDecimal.valueOf(30.0));


        Conta contaDestino = new Conta();
        contaDestino.setId(2L);
        when(usuario.getIdUsuario()).thenReturn(123L);
        when(contaRepository.findByUsuario_IdUsuarioAndId(123L, 1L))
                .thenReturn(Optional.of(contaOrigem));

        when(usuario.getIdUsuario()).thenReturn(123L);

        assertThrows(IllegalArgumentException.class, () -> {
            contaService.transferir(1L, 2L, 50.0, sessionMock);
        }, "Saldo insuficiente não gerou a exceção esperada");
    }

    @Test
    void testTransferir() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);
        when(request.getSession()).thenReturn(sessionMock);

        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(123L);
        when(usuarioMock.getLogin()).thenReturn("user123");
        when(usuarioMock.getPerfilUsuario()).thenReturn(PerfilUsuario.CORRENTISTA);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

        Conta contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setUsuario(usuarioMock);
        contaOrigem.setSaldo(BigDecimal.valueOf(100.0));

        Conta contaDestino = new Conta();
        contaDestino.setId(2L);
        contaDestino.setSaldo(BigDecimal.ZERO);


        when(contaRepository.findByUsuario_IdUsuarioAndId(123L, 1L))
                .thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdWithLock(2L)).thenReturn(Optional.of(contaDestino));

        contaService.transferir(1L, 2L, 50.0, sessionMock);

        assertEquals(0, contaOrigem.getSaldo().compareTo(BigDecimal.valueOf(50.0)));
        assertEquals(0, contaDestino.getSaldo().compareTo(BigDecimal.valueOf(50.0)));
        verify(transacaoService, times(2)).registrarTransacao(any(Conta.class), any(), any());
    }

}

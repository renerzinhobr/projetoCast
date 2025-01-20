package br.com.teste.cast.controller;

import br.com.teste.cast.constantes.Mensagem;
import br.com.teste.cast.constantes.PerfilUsuario;
import br.com.teste.cast.dto.ContaDTO;
import br.com.teste.cast.exception.ContaNaoPertenceAoUsuarioException;
import br.com.teste.cast.model.Conta;
import br.com.teste.cast.model.Usuario;
import br.com.teste.cast.service.IAuthService;
import br.com.teste.cast.service.IContaService;
import br.com.teste.cast.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import util.Util;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private IContaService contaService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private HttpSession session;

    @Autowired
    private IAuthService authService;

    @GetMapping("/listar")
    public String listarContas(Model model,HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.CORRENTISTA.getRole())) {

            model.addAttribute("contas",contaService.buscarTodasContasUsuario(session));
            return "contas/listar";
        } else {
            return "naoAutorizado";
        }

    }

    @GetMapping("/listarAdmin")
    public String listarContasAdmin(Model model, HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.ADMIN.getRole())) {
        model.addAttribute("contas", contaService.buscarTodasContas());
        return "contas/listarAdmin";
        } else {
            return "naoAutorizado";
        }
    }

    @GetMapping("/formCriarConta")
    public String exibirFormularioCriacaoConta(Model model,HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.ADMIN.getRole())) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "contas/criar";
        } else {
            return "naoAutorizado";
        }
    }

    @GetMapping("/formEditarConta/{id}")
    public String exibirFormularioEditarConta(@PathVariable("id") Long id,Model model,HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.ADMIN.getRole())) {
            Conta conta = contaService.buscarContaPorId(id);
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("conta", conta);
            return "contas/editar";
        } else {
            return "naoAutorizado";
        }
    }


    @PostMapping("/salvar")
    public ResponseEntity<String> salvarConta(@Valid @RequestBody ContaDTO contaDTO,HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.ADMIN.getRole())) {
            try {
                contaService.salvarConta(contaDTO);
                return ResponseEntity.ok(Mensagem.CONTA_SALVA.getTexto());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Mensagem.ERRO_SALVAR_CONTA.getTexto().concat(e.getMessage()));
            }

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Mensagem.ERRO_USUARIO_NAO_AUTORIZADO.getTexto());
        }
    }



    @PostMapping("/creditar/{id}")

    public ResponseEntity<String> creditarValor(@PathVariable("id") Long id, @RequestBody Map<String, Double> payload) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.CORRENTISTA.getRole())) {
        try {
            Double valor = payload.get("valor");
            Util.validarValor(valor, Mensagem.ERRO_VALOR_INVALIDO.getTexto());
            Conta conta = contaService.buscarContaPorId(id);
            contaService.creditar(conta.getId(), valor);
            return ResponseEntity.ok(Mensagem.CREDITO_EFETIVADO.getTexto());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Mensagem.ERRO_CREDITAR_CONTA.getTexto().concat(e.getMessage()));
        }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Mensagem.ERRO_USUARIO_NAO_AUTORIZADO.getTexto());
        }
    }


    @GetMapping("/formOperacoes/{id}")
    public String exibirFormularioOperacoes(@PathVariable("id") Long id, Model model,HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.CORRENTISTA.getRole())) {
            try {
        Conta contaOp = contaService.buscarContaPorIdParaOperacao(id, session);
            List<Conta> contasDestino = contaService.buscarTodasContas();
            contasDestino.removeIf(conta -> conta.getId().equals(contaOp.getId()));
                model.addAttribute("conta", contaOp);
                model.addAttribute("contasDestino", contasDestino);
        return "contas/formOperacoes";
            } catch (ContaNaoPertenceAoUsuarioException e) {
                return "/contas/contaNaoPertence";
            } catch (Exception e) {
                return "naoAutorizado";
            }

    } else {
        return "naoAutorizado";
    }
    }


    @PostMapping("/debitar/{id}")
    public ResponseEntity<String> debitarValor(@PathVariable("id") Long id, @RequestBody Map<String, Double> payload, HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.CORRENTISTA.getRole())) {
        try {
            Conta conta = contaService.buscarContaPorIdParaOperacao(id, session);
            Double valor = payload.get("valor");
            Util.validarValor(valor, Mensagem.ERRO_VALOR_INVALIDO.getTexto());

            contaService.debitar(conta.getId(), valor);
            return ResponseEntity.ok(Mensagem.DEBITO_EFETIVADO.getTexto());

        } catch (ContaNaoPertenceAoUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Mensagem.ERRO_DEBITAR_CONTA.getTexto().concat(e.getMessage()));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Mensagem.ERRO_DEBITAR_CONTA.getTexto().concat(e.getMessage()));
        }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Mensagem.ERRO_USUARIO_NAO_AUTORIZADO.getTexto());
        }
    }


    @GetMapping("/formTrasferir/{id}")
    public String exibirFormularioTransferir(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.CORRENTISTA.getRole())) {
        try {
            Conta contaOrigem = contaService.buscarContaPorIdParaOperacao(id, session);
            List<Conta> contasDestino = contaService.buscarTodasContas();
            contasDestino.removeIf(conta -> conta.getId().equals(contaOrigem.getId()));
            model.addAttribute("contaOrigem", contaOrigem);
            model.addAttribute("contasDestino", contasDestino);
            return "contas/formTrasferir";
        } catch (ContaNaoPertenceAoUsuarioException e) {
            return "/contas/contaNaoPertence";
        } catch (Exception e) {
            return "naoAutorizado";
        }
        } else {
            return "naoAutorizado";
        }
    }

    @PostMapping("/transferir/{id}")
    public ResponseEntity<String> transferirValor(@PathVariable("id") Long id, @RequestBody Map<String, Double> payload,HttpSession session){
        if (authService.VerificaRoleNaSession(session, PerfilUsuario.CORRENTISTA.getRole())) {
        try {
            Double valor = payload.get("valor");
            Util.validarValor(valor, Mensagem.ERRO_VALOR_INVALIDO.getTexto());
            Double idContaDestino = payload.get("idContaDestino");
            contaService.transferir(id, idContaDestino.longValue(), valor,session);
            return ResponseEntity.ok(Mensagem.TRANSFERENCIA_EFETIVADA.getTexto());
        }catch (ContaNaoPertenceAoUsuarioException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Mensagem.ERRO_TRANSFERIR_CONTA.getTexto().concat(e.getMessage()));
        }
        catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Mensagem.ERRO_TRANSFERIR_CONTA.getTexto().concat(e.getMessage()));
        }

    }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Mensagem.ERRO_USUARIO_NAO_AUTORIZADO.getTexto());
        }
    }

}

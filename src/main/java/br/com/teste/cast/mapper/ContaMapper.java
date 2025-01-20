package br.com.teste.cast.mapper;

import br.com.teste.cast.dto.ContaDTO;
import br.com.teste.cast.model.Conta;
import org.springframework.stereotype.Component;

@Component
public class ContaMapper {


    public ContaDTO toDTO(Conta conta) {
        if (conta == null) {
            return null;
        }

        return ContaDTO.builder()
                .id(conta.getId())
                .saldo(conta.getSaldo())
                .numeroContaCorrente(conta.getNumeroContaCorrente())
                .idUsuario(conta.getUsuario().getIdUsuario().longValue())
                .dataHoraInclusao(conta.getDataHoraInclusao())
                .digitoContaCorrente(conta.getDigitoContaCorrente())
                .tipoContaCorrente(conta.getTipoContaCorrente())
                .indAtivo(conta.isIndAtivo())
                .version(conta.getVersion())
                .build();
    }


    public Conta toEntity(ContaDTO contaDTO) {
        if (contaDTO == null) {
            return null;
        }

        Conta conta = new Conta();
        conta.setId(contaDTO.getId());
        conta.setSaldo(contaDTO.getSaldo());
        conta.setNumeroContaCorrente(contaDTO.getNumeroContaCorrente());
        conta.setDataHoraInclusao(contaDTO.getDataHoraInclusao());
        conta.setDigitoContaCorrente(contaDTO.getDigitoContaCorrente());
        conta.setTipoContaCorrente(contaDTO.getTipoContaCorrente());
        conta.setIndAtivo(contaDTO.isIndAtivo());
        conta.setUsuario(contaDTO.getUsuario());
        conta.setVersion(contaDTO.getVersion());
        return conta;
    }
}
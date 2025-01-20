package br.com.teste.cast.dto;

import br.com.teste.cast.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {

    private Long id;
    private BigDecimal saldo;
    private Long numeroContaCorrente;
    private Long idUsuario;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraAtualizacao;
    private Short digitoContaCorrente;
    private Byte tipoContaCorrente;
    private Usuario usuario;
    private boolean indAtivo;
    private Integer version;
}
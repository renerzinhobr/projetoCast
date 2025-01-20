package br.com.teste.cast.model;

import br.com.teste.cast.constantes.TipoPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDCONTA", referencedColumnName = "ID")
    private Conta conta;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_TRANSACAO")
    private TipoPagamento tipoPagamento;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @Column(name = "DATA_TRANSACAO")
    private LocalDateTime dataTransacao;

    public Transacao(Conta conta, TipoPagamento tipoPagamento, BigDecimal valor) {
        this.conta = conta;
        this.tipoPagamento = tipoPagamento;
        this.valor = valor;
        this.dataTransacao = LocalDateTime.now();
    }
}
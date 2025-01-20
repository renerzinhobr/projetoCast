package br.com.teste.cast.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 15, scale = 2)
    private BigDecimal saldo;

    @Version
    private Integer version;

    @Column(name = "NUMEROCONTACORRENTE", unique = true)
    private Long numeroContaCorrente;

    @ManyToOne
    @JoinColumn(name = "IDUSUARIO", referencedColumnName = "IDUSUARIO")
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "DATAHORAINCLUSAO", updatable = false)
    private LocalDateTime dataHoraInclusao;

    @Column(name = "DIGITOCONTACORRENTE")
    private Short digitoContaCorrente;

    @Column(name = "TIPOCONTACORRENTE")
    private Byte tipoContaCorrente;

    @Column(name = "INDATIVO", nullable = false)
    private boolean indAtivo = true;

    public Conta(Usuario usuario, Long numeroContaCorrente, Short digitoContaCorrente, Byte tipoContaCorrente) {
        this.saldo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.usuario = usuario;
        this.numeroContaCorrente = numeroContaCorrente;
        this.digitoContaCorrente = digitoContaCorrente;
        this.tipoContaCorrente = tipoContaCorrente;
    }

    public void creditar(BigDecimal valor) {
        if (valor != null) {
            this.saldo = this.saldo.add(valor).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void debitar(BigDecimal valor) {
        if (valor != null && this.saldo.compareTo(valor) >= 0) {
            this.saldo = this.saldo.subtract(valor).setScale(2, RoundingMode.HALF_UP);
        } else {
            throw new IllegalArgumentException("Saldo insuficiente! Saldo Atual:"+this.saldo);
        }
    }

    public void transferir(Conta destino, BigDecimal valor) {
        this.debitar(valor);
        destino.creditar(valor);
    }
}
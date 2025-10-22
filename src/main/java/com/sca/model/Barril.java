package com.sca.model;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sca.constantes.ExpresionRegular;
import com.sca.validator.ValidarExpresionesRegulares;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ✅ Activamos comparación explícita
@ToString
@Entity
@Table(name = "barril")
public class Barril {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // ✅ Esto permite que el Set<Barril> compare por ID
    private Long id;

    @Column(name = "litros", nullable = false)
    private Integer litros;

    @ValidarExpresionesRegulares(
        customMessage = "El estado no es válido",
        expresionRegular = ExpresionRegular.BARRIL_ESTADO
    )
    @Column(name = "estado")
    private String estado;

    @Column(name = "notas")
    private String notas;

    @Column(name = "session_reserva")
    private String sessionReserva;

    @Column(name = "timestamp_reserva")
    private LocalDateTime timestampReserva;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    @JsonBackReference
    private Lote lote;

    @Column(name = "lote_id", insertable = false, updatable = false)
    private Long loteId;

    public Barril() {}

    public Barril(Long id, Integer litros, String estado, String notas, Lote lote, String sessionReserva, LocalDateTime timestampReserva) {
        this.id = id;
        this.litros = litros;
        this.estado = estado;
        this.notas = notas;
        this.lote = lote;
        this.sessionReserva = sessionReserva;
        this.timestampReserva = timestampReserva;
    }

    public Barril(Integer litros, String estado, String notas) {
        this.litros = litros;
        this.estado = estado;
        this.notas = notas;
    }
}

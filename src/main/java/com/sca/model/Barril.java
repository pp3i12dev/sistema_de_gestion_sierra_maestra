package com.sca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "barril")
public class Barril {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // ✅ NUEVO: Campo para la sesión de reserva
    @Column(name = "session_reserva")
    private String sessionReserva;

    // ✅ NUEVO: Campo para el timestamp de reserva  
    @Column(name = "timestamp_reserva")
    private LocalDateTime timestampReserva;

    // 🔹 Relación ManyToOne con Lote
    @ManyToOne
    @JoinColumn(name = "lote_id")
    @JsonBackReference
    private Lote lote;

    // 🔹 Campo solo de lectura para obtener el ID del lote directamente
    @Column(name = "lote_id", insertable = false, updatable = false)
    private Long loteId;

    // 🔹 Constructores (actualizados)
    public Barril() {
    }

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
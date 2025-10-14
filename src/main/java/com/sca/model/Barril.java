package com.sca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    @ValidarExpresionesRegulares(customMessage = "El estado no es válido", expresionRegular = ExpresionRegular.BARRIL_ESTADO)
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "notas")
    private String notas;

    // Relación ManyToOne con Lote
    @ManyToOne
    @JoinColumn(name = "lote_id")  // Crea la columna lote_id en la tabla barril para asociar barriles con un lote
    @JsonBackReference
    private Lote lote;

    // Constructores
    public Barril(Long id, Integer litros, String estado, String notas, Lote lote) {
        this.id = id;
        this.litros = litros;
        this.estado = estado;
        this.notas = notas;
        this.lote = lote;
    }

    public Barril() {
    }

    public Barril(Integer litros, String estado, String notas) {
        this.litros = litros;
        this.estado = estado;
        this.notas = notas;
    }
}

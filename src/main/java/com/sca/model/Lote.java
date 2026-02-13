package com.sca.model;
// import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "lote")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "cerveza", nullable = false)
    // private String cerveza;

    @NotNull(message = "La cerveza es obligatoria")
    @ManyToOne
    @JoinColumn(name = "cerveza_id", nullable = false)  // FK en tabla LOTE
    private Cerveza cerveza;

    @NotNull(message = "La cantidad de litros es obligatoria")
    @Min(value = 1, message = "La cantidad de litros debe ser mayor a 0")
    @Column(name = "cantidad_litros", nullable = false)
    private Integer cantidadLitros;

    @NotEmpty(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false)
    @ValidarExpresionesRegulares(customMessage = "El estado no es válido", expresionRegular = ExpresionRegular.LOTE_ESTADO)
    private String estado;

    @Column(name = "notas")
    private String notas;

    @NotNull(message = "La fecha de carga es obligatoria")
    @Column(name = "fecha_carga", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Argentina/Buenos_Aires")
    private Date fechaCarga;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Argentina/Buenos_Aires")
    private Date fechaVencimiento;

    @NotNull(message = "La fecha de carga en madurador es obligatoria")
    @Column(name = "fecha_carga_madurador", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Argentina/Buenos_Aires")
    private Date fechaCargaMadurador;
    
    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull(message = "La lista de barriles no puede ser nula")
    @JsonManagedReference
    private Set<Barril> barriles = new HashSet<>();

    // @OneToMany(mappedBy = "lote")
    // private Set<Madurador> madurador;

    public Lote() {}

    public Lote(Cerveza cerveza, Integer cantidadLitros, String estado, String notas, Date fechaCarga, Date fechaVencimiento, Date fechaCargaMadurador) {
        this.cerveza = cerveza;
        this.cantidadLitros = cantidadLitros;
        this.estado = estado;
        this.notas = notas;
        this.fechaCarga = fechaCarga;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaCargaMadurador = fechaCargaMadurador;
    }
}